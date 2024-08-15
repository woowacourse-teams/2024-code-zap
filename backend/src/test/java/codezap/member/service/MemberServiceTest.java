package codezap.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.dto.request.SignupRequest;
import codezap.member.dto.response.FindMemberResponse;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;

public class MemberServiceTest {

    private final MemberRepository memberRepository = new FakeMemberRepository();
    private final CategoryRepository categoryRepository = new FakeCategoryRepository();
    private final MemberService memberService = new MemberService(memberRepository, categoryRepository);

    @Nested
    @DisplayName("이메일 중복 검사 테스트")
    class CheckEmail {

        @Test
        @DisplayName("이메일 중복 검사 통과: 사용가능한 이메일")
        void assertUniqueEmail() {
            var email = "code@zap.com";

            assertThatCode(() -> memberService.assertUniqueEmail(email))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이메일 중복 검사 실패: 중복된 이메일")
        void assertUniqueEmail_fail_duplicate() {
            var savedMember = new Member(1L, "code@zap.com", "password", "zappy");
            memberRepository.save(savedMember);

            assertThatThrownBy(() -> memberService.assertUniqueEmail("code@zap.com"))
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

            assertThatCode(() -> memberService.assertUniqueUsername(username))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("사용자명 중복 검사 실패: 중복된 사용자명")
        void assertUniqueUsername_fail_duplicate() {
            var savedMember = new Member(1L, "code@zap.com", "password", "zappy");
            memberRepository.save(savedMember);

            assertThatThrownBy(() -> memberService.assertUniqueUsername("zappy"))
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

            var actual = memberService.signup(request);

            var expect = new Member(1L, request.email(), request.password(), request.username());
            assertThat(actual).isEqualTo(expect);
        }

        @Test
        @DisplayName("회원가입 실패: 이메일 중복")
        void signup_fail_email_duplicate() {
            var saved = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(saved);
            var request = new SignupRequest("code@zap.com", "password", "chorong");

            assertThatThrownBy(() -> memberService.signup(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessageContaining("이메일이 이미 존재합니다.");
        }

        @Test
        @DisplayName("회원가입 실패: 사용자명 중복")
        void signup_fail_username_duplicate() {
            var saved = new Member("code@zap.com", "pw1234", "zappy");
            memberRepository.save(saved);
            var request = new SignupRequest("chorong@zangsu.com", "password", "zappy");

            assertThatThrownBy(() -> memberService.signup(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessageContaining("사용자명이 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class findMember {

        @Test
        @DisplayName("회원 정보 조회 성공")
        void findMember() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            assertThat(memberService.findMember(MemberDto.from(member), member.getId()))
                    .isEqualTo(FindMemberResponse.from(member));
        }

        @Test
        @DisplayName("회원 정보 조회 실패: 본인 정보가 아닌 경우")
        void findMember_Throw() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            assertThatThrownBy(() -> memberService.findMember(MemberDto.from(member), member.getId() + 1))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("본인의 정보만 조회할 수 있습니다.");
        }
    }
}
