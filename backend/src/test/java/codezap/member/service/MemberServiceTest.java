package codezap.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Stream;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;

import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.LoginRequest;
import codezap.member.dto.MemberDto;
import codezap.member.dto.SignupRequest;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;

public class MemberServiceTest {

    private final MemberRepository memberRepository = new FakeMemberRepository();
    private final CategoryRepository categoryRepository = new FakeCategoryRepository();
    private final AuthService authService = new AuthService(memberRepository);
    private final MemberService sut = new MemberService(memberRepository, authService, categoryRepository);

    @Nested
    @DisplayName("이메일 중복 검사 테스트")
    class CheckEmail {

        @Test
        @DisplayName("이메일 중복 검사 통과: 사용가능한 이메일")
        void assertUniqueEmail() {
            var email = "code@zap.com";

            assertThatCode(() -> sut.assertUniqueEmail(email))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이메일 중복 검사 실패: 중복된 이메일")
        void assertUniqueEmail_fail_duplicate() {
            var savedMember = new Member(1L, "code@zap.com", "password", "zappy");
            memberRepository.save(savedMember);

            assertThatThrownBy(() -> sut.assertUniqueEmail("code@zap.com"))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이메일이 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("사용자명 중복 검사 테스트")
    class CheckUsername {

        @Test
        @DisplayName("사용자명 중복 검사 통과: 사용가능한 사용자명")
        void assertUniqueUsername() {
            var username = "zappy";

            assertThatCode(() -> sut.assertUniqueUsername(username))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("사용자명 중복 검사 실패: 중복된 사용자명")
        void assertUniqueUsername_fail_duplicate() {
            var savedMember = new Member(1L, "code@zap.com", "password", "zappy");
            memberRepository.save(savedMember);

            assertThatThrownBy(() -> sut.assertUniqueUsername("zappy"))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("사용자명이 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTest {

        @Test
        @DisplayName("회원가입 성공")
        void signup() {
            var request = new SignupRequest("code@zap.com", "password", "chorong");

            var actual = sut.signup(request);

            var expect = new Member(1L, request.email(), request.password(), request.username());
            assertThat(actual).isEqualTo(expect);
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
        @DisplayName("로그인 성공")
        void login() {
            var member = new Member(1L, "code@zap.com", "pw1234", "zappy");
            memberRepository.save(member);
            var request = new LoginRequest(member.getEmail(), member.getPassword());

            var actual = sut.login(request);

            var expect = MemberDto.from(member);
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
            var basicAuthCredentials = encodeToBase64(member.getEmail() + ":" + member.getPassword());
            var basicAuthCookie = new Cookie(HttpHeaders.AUTHORIZATION, basicAuthCredentials);
            var cookies = new Cookie[]{basicAuthCookie};

            assertThatCode(() -> sut.checkLogin(cookies))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @DisplayName("쿠키 인증 실패: 쿠키 값 오류")
        @MethodSource
        void checkLogin_fail_wrong_cookie_value(String wrongCredentials) {
            var member = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(member);
            var basicAuthCookie = new Cookie(HttpHeaders.AUTHORIZATION, wrongCredentials);
            var cookies = new Cookie[]{basicAuthCookie};

            assertThatThrownBy(() -> sut.checkLogin(cookies))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("인증에 실패했습니다.");
        }

        static Stream<String> checkLogin_fail_wrong_cookie_value() {
            return Stream.of(
                    encodeToBase64("wrong@email.kr:nopassword"),
                    encodeToBase64("Hello world!")
            );
        }

        static String encodeToBase64(String plainText) {
            return Base64.getEncoder().encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Nested
    @DisplayName("멤버 정보 체크")
    class validateMemberIdentity {
        @Test
        @DisplayName("멤버 정보 체크 성공")
        void validateMemberIdentitySuccess() {
            var savedMember = new Member(1L, "code@zap.com", "password", "zappy");
            memberRepository.save(savedMember);
            MemberDto memberDto = MemberDto.from(savedMember);
            Long memberId = 1L;

            assertThatCode(() -> sut.validateMemberIdentity(memberDto, memberId))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("멤버 정보 체크 실패: 두 id가 다름")
        void validateMemberIdentityFailNotSame() {
            var savedMember = new Member(1L, "code@zap.com", "password", "zappy");
            memberRepository.save(savedMember);
            MemberDto memberDto = MemberDto.from(savedMember);
            Long memberId = 2L;

            assertThatThrownBy(() -> sut.validateMemberIdentity(memberDto, memberId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("인증 정보에 포함된 멤버 ID와 파라미터로 받은 멤버 ID가 다릅니다.");
        }


        @Test
        @DisplayName("멤버 정보 체크 실패: 해당 멤버가 DB에 없음")
        void validateMemberIdentityFailNo() {
            var savedMember = new Member(1L, "code@zap.com", "password", "zappy");
            MemberDto memberDto = MemberDto.from(savedMember);
            Long memberId = 1L;

            assertThatThrownBy(() -> sut.validateMemberIdentity(memberDto, memberId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("로그인 정보가 잘못되었습니다.");
        }
    }

}
