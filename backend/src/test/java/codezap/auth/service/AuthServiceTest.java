package codezap.auth.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import codezap.auth.dto.LoginAndCredentialDto;
import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.LoginResponse;
import codezap.auth.provider.CredentialProvider;
import codezap.auth.provider.basic.BasicAuthCredentialProvider;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.auth.encryption.PasswordEncryptor;
import codezap.auth.encryption.SHA2PasswordEncryptor;

public class AuthServiceTest {
    private final MemberRepository memberRepository = new FakeMemberRepository();
    private final PasswordEncryptor passwordEncryptor = new SHA2PasswordEncryptor();
    private final CredentialProvider credentialProvider = new BasicAuthCredentialProvider(memberRepository);
    private final AuthService authService = new AuthService(credentialProvider, memberRepository, passwordEncryptor);

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        @Test
        @DisplayName("로그인 성공")
        void login() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            LoginRequest loginRequest = new LoginRequest(member.getName(), MemberFixture.getFixturePlainPassword());

            LoginAndCredentialDto loginAndCredentialDto = authService.login(loginRequest);

            assertAll(
                    () -> assertEquals(loginAndCredentialDto.loginResponse(), LoginResponse.from(member)),
                    () -> assertEquals(loginAndCredentialDto.credential(), credentialProvider.createCredential(member))
            );
        }

        @Test
        @DisplayName("로그인 실패: 아이디 오류")
        void login_WithInvalidname_ThrowsException() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            String wrongname = "wrong" + member.getName();

            LoginRequest loginRequest = new LoginRequest(wrongname, member.getPassword());

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("존재하지 않는 아이디 " + wrongname + " 입니다.");
        }

        @Test
        @DisplayName("로그인 실패: 비밀번호 오류")
        void login_WithInvalidPassword_ThrowsException() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            LoginRequest loginRequest = new LoginRequest(member.getName(), member.getPassword() + "wrong");

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인해주세요.");
        }
    }

    @Nested
    @DisplayName("쿠키 인증 테스트")
    class CheckLoginTest {

        @Test
        @DisplayName("쿠키 인증 성공")
        void checkLogin() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            String basicAuthCredentials = credentialProvider.createCredential(member);

            assertThatCode(() -> authService.checkLogin(basicAuthCredentials))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("쿠키 인증 실패: 잘못된 크레덴셜")
        void checkLogin_WithInvalidCredential_ThrowsException() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            String invalidCredential = HttpHeaders.encodeBasicAuth(
                    member.getName(),
                    member.getPassword() + "wrong",
                    StandardCharsets.UTF_8
            );

            assertThatThrownBy(() -> authService.checkLogin(invalidCredential))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessageContaining("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }
}
