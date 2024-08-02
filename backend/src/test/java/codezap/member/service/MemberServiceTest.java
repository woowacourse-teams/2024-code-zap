package codezap.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.member.dto.LoginRequest;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.dto.SignupRequest;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
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
        @DisplayName("로그인 성공: 액세스 토큰 반환")
        void login() {
            var saved = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(saved);
            var request = new LoginRequest(saved.getEmail(), saved.getPassword());

            var token = sut.login(request);

            assertThat(token).isNotNull();
        }
    }
}
