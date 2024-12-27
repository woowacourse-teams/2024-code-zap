package codezap.auth.service;

import codezap.auth.dto.LoginMember;
import codezap.auth.dto.request.LoginRequest;
import codezap.auth.encryption.PasswordEncryptor;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncryptor passwordEncryptor;

    public LoginMember login(LoginRequest loginRequest) {
        var member = memberRepository.fetchByName(loginRequest.name());
        validateCorrectPassword(member, loginRequest.password());
        return LoginMember.from(member);
    }

    private void validateCorrectPassword(Member member, String password) {
        var salt = member.getSalt();
        var encryptedPassword = passwordEncryptor.encrypt(password, salt);
        if (!member.matchPassword(encryptedPassword)) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_PASSWORD, "로그인에 실패하였습니다. 비밀번호를 확인해주세요.");
        }
    }
}
