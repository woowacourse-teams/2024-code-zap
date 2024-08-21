package codezap.auth.provider.basic;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import codezap.auth.provider.CredentialProvider;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BasicAuthCredentialProvider implements CredentialProvider {

    private final MemberRepository memberRepository;

    @Override
    public String createCredential(Member member) {
        return HttpHeaders.encodeBasicAuth(member.getName(), member.getPassword(), StandardCharsets.UTF_8);
    }

    @Override
    public Member extractMember(String credential) {
        String[] nameAndPassword = BasicAuthDecoder.decodeBasicAuth(credential);
        Member member = memberRepository.fetchByname(nameAndPassword[0]);
        checkMatchPassword(member, nameAndPassword[1]);
        return member;
    }

    private void checkMatchPassword(Member member, String password) {
        if (!member.matchPassword(password)) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_PASSWORD, "비밀번호를 확인해주세요.");
        }
    }
}
