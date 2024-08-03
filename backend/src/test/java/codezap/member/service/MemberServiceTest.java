package codezap.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.LoginRequest;
import codezap.member.dto.SignupRequest;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;

public class MemberServiceTest {

    private final MemberRepository memberRepository = new FakeMemberRepository();
    private final MemberService sut = new MemberService(memberRepository);

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTest {

        @Test
        @DisplayName("회원가입 성공")
        void signup() {
            var request = new SignupRequest("code@zap.com", "password", "chorong");

            sut.signup(request);

            assertThat(memberRepository.findAll()).hasSize(1);
        }

        @Test
        @DisplayName("회원가입 실패: 이메일 중복")
        void signup_fail_email_duplicate() {
            var saved = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(saved);
            var request = new SignupRequest("code@zap.com", "password", "chorong");

            assertThatThrownBy(() -> sut.signup(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessageContaining("이메일이 이미 존재합니다.");
        }

        @Test
        @DisplayName("회원가입 실패: 사용자명 중복")
        void signup_fail_username_duplicate() {
            var saved = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(saved);
            var request = new SignupRequest("chorong@zangsu.com", "password", "zappy");

            assertThatThrownBy(() -> sut.signup(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessageContaining("사용자명이 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        @Test
        @DisplayName("로그인 성공: Basic authentication 쿠키 값 반환")
        void login() {
            // given
            var member = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(member);
            var request = new LoginRequest(member.getEmail(), member.getPassword());

            // when
            var actual = sut.login(request);

            // then
            var basicAuth = member.getEmail() + ":" + member.getPassword();
            var expect = Base64.getEncoder().encodeToString(basicAuth.getBytes(StandardCharsets.UTF_8));
            assertThat(actual).isEqualTo(expect);
        }

        @Test
        @DisplayName("로그인 실패: 비밀번호 오류")
        void login_fail_wrong_password() {
            var member = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(member);
            var request = new LoginRequest(member.getEmail(), "wrongpassword");

            assertThatThrownBy(() -> sut.login(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("인증에 실패했습니다.");
        }
    }

    @Nested
    @DisplayName("쿠키 인증 테스트")
    class CheckLoginTest {

        @Test
        @DisplayName("쿠키 인증 성공")
        void checkLogin() {
            var member = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(member);
            var basicAuthCredentials = HttpHeaders.encodeBasicAuth(
                    member.getEmail(),
                    member.getPassword(),
                    StandardCharsets.UTF_8
            );
            var basicAuthCookie = new Cookie(HttpHeaders.AUTHORIZATION, basicAuthCredentials);
            var cookies = new Cookie[]{basicAuthCookie};

            assertThatCode(() -> sut.authorizeByCookie(cookies))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("쿠키 인증 실패: 쿠키 값 오류")
        void checkLogin_fail_wrong_cookie_value() {
            var member = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(member);
            var wrongCredentials = HttpHeaders.encodeBasicAuth(
                    "wrong@email.kr",
                    "nopassword",
                    StandardCharsets.UTF_8
            );
            var basicAuthCookie = new Cookie(HttpHeaders.AUTHORIZATION, wrongCredentials);
            var cookies = new Cookie[]{basicAuthCookie};

            assertThatThrownBy(() -> sut.authorizeByCookie(cookies))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("인증에 실패했습니다.");
        }
    }
}
