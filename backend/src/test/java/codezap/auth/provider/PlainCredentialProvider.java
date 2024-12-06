package codezap.auth.provider;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import codezap.member.domain.Member;

public class PlainCredentialProvider implements CredentialProvider {

    private static final String DELIMITER = URLEncoder.encode(";", StandardCharsets.UTF_8);

    @Override
    public String createCredential(Member member) {
        return Stream.of(Long.toString(member.getId()), member.getName(), member.getPassword(), member.getSalt())
                .map(value -> URLEncoder.encode(value, StandardCharsets.UTF_8))
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Member extractMember(String credential) {
        List<String> memberInfo = Arrays.stream(credential.split(DELIMITER))
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
        return "stub";
    }
}
