package codezap.auth.provider.basic;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import codezap.auth.provider.CredentialProvider;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BasicAuthCredentialProvider implements CredentialProvider {

    private final MemberRepository memberRepository;

    @Override
    public String createCredential(Member member) {
        return HttpHeaders.encodeBasicAuth(member.getLoginId(), member.getPassword(), StandardCharsets.UTF_8);
    }

    @Override
    public Member extractMember(String credential) {
        String[] loginIdAndPassword = BasicAuthDecoder.decodeBasicAuth(credential);
        Member member = memberRepository.fetchByLoginId(loginIdAndPassword[0]);
        checkMatchPassword(member, loginIdAndPassword[1]);
        return member;
    }

    private void checkMatchPassword(Member member, String password) {
        if (!member.matchPassword(password)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }
}
