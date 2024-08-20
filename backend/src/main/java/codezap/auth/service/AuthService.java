package codezap.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import codezap.auth.dto.LoginAndCredentialDto;
import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.LoginResponse;
import codezap.auth.provider.CredentialProvider;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CredentialProvider credentialProvider;
    private final MemberRepository memberRepository;

    public LoginAndCredentialDto login(LoginRequest loginRequest) {
        Member member = getVerifiedMember(loginRequest.name(), loginRequest.password());
        String credential = credentialProvider.createCredential(member);
        return new LoginAndCredentialDto(LoginResponse.from(member), credential);
    }

    private Member getVerifiedMember(String name, String password) {
        Member member = memberRepository.fetchByname(name);
        validateCorrectPassword(member, password);
        return member;
    }

    private void validateCorrectPassword(Member member, String password) {
        if (!member.matchPassword(password)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인해주세요.");
        }
    }

    public void checkLogin(String credential) {
        credentialProvider.extractMember(credential);
    }
}
