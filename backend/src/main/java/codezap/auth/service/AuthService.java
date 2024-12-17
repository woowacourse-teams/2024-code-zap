package codezap.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.auth.dto.LoginAndCredentialDto;
import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.LoginResponse;
import codezap.auth.encryption.PasswordEncryptor;
import codezap.auth.provider.CredentialProvider;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final CredentialProvider credentialProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncryptor passwordEncryptor;

    public LoginAndCredentialDto login(LoginRequest request) {
        Member member = memberRepository.fetchByName(request.name());
        validatePassword(member, request.password());
        String credential = credentialProvider.createCredential(member);
        return new LoginAndCredentialDto(LoginResponse.from(member), credential);
    }

    private void validatePassword(Member member, String password) {
        String salt = member.getSalt();
        String encryptedPassword = passwordEncryptor.encrypt(password, salt);
        if (!member.matchPassword(encryptedPassword)) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_PASSWORD, "로그인에 실패하였습니다. 비밀번호를 확인해주세요.");
        }
    }

    public void checkLogin(String credential) {
        credentialProvider.extractMember(credential);
    }
}
