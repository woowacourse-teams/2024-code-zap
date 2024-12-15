package codezap.auth.provider;

import codezap.auth.dto.LoginMember;
import codezap.auth.manager.Credential;
import codezap.member.domain.Member;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlainCredentialProvider implements CredentialProvider {

    private static final String DELIMITER = URLEncoder.encode(";", StandardCharsets.UTF_8);

    @Override
    public Credential createCredential(LoginMember loginMember) {
        String credentialValue = Stream.of(Long.toString(loginMember.id()), loginMember.name(), loginMember.password(),
                        loginMember.salt())
                .map(value -> URLEncoder.encode(value, StandardCharsets.UTF_8))
                .collect(Collectors.joining(DELIMITER));
        return new Credential(getType(), credentialValue);
    }

    @Override
    public Member extractMember(Credential credential) {
        List<String> memberInfo = Arrays.stream(credential.value().split(DELIMITER))
                .map(value -> URLDecoder.decode(value, StandardCharsets.UTF_8))
                .toList();
        return new Member(
                Long.parseLong(memberInfo.get(0)),
                memberInfo.get(1),
                memberInfo.get(2),
                memberInfo.get(3)
        );
    }

    @Override
    public String getType() {
        return "Plain";
    }
}
