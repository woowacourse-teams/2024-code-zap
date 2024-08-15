package codezap.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @DisplayName("회원가입 테스트")
    class SignupTest {

        @Test
        @DisplayName("회원가입 성공")
        void signup() {
            Member member = MemberFixture.memberFixture();
            SignupRequest signupRequest = new SignupRequest(member.getLoginId(), member.getPassword());

            assertEquals(memberService.signup(signupRequest), 1L);
        }

        @Test
        @DisplayName("회원가입 실패: 아이디 중복")
        void signup_fail_loginId_duplicate() {
            Member savedMember = memberRepository.save(MemberFixture.memberFixture());
            SignupRequest signupRequest = new SignupRequest(savedMember.getLoginId(), savedMember.getPassword());

            assertThatThrownBy(() -> memberService.signup(signupRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessageContaining("아이디가 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("아이디 중복 검사 테스트")
    class AssertUniqueLoginId {

        @Test
        @DisplayName("아이디 중복 검사 통과: 사용가능한 아이디")
        void assertUniqueLoginId() {
            String loginId = "code";

            assertThatCode(() -> memberService.assertUniqueLoginId(loginId))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("아이디 중복 검사 실패: 중복된 아이디")
        void assertUniqueLoginId_fail_duplicate() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            assertThatThrownBy(() -> memberService.assertUniqueLoginId(member.getLoginId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("아이디가 이미 존재합니다.");
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
