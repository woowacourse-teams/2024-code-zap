package codezap.auth.provider.basic;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import codezap.auth.provider.CredentialProvider;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;

@Component
public class BasicAuthCredentialProvider implements CredentialProvider {

    private final MemberRepository memberRepository;

    public BasicAuthCredentialProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public String createCredential(Member member) {
        return HttpHeaders.encodeBasicAuth(member.getEmail(), member.getPassword(), StandardCharsets.UTF_8);
    }

    @Override
    public Member extractMember(String credential) {
        String[] emailAndPassword = BasicAuthDecoder.decodeBasicAuth(credential);
        Member member = memberRepository.fetchByEmail(emailAndPassword[0]);
        checkMatchPassword(member, emailAndPassword[1]);
        return member;
    }

    private void checkMatchPassword(Member member, String password) {
        if (!member.matchPassword(password)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다.");
        }
    }
}
