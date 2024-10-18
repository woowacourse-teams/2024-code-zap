package codezap.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.ServiceTest;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.request.SignupRequest;
import codezap.member.dto.response.FindMemberResponse;
import codezap.member.fixture.MemberFixture;
import codezap.template.domain.Template;

class MemberServiceTest extends ServiceTest {

    @Autowired
    private MemberService memberService;

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTest {

        @Test
        @DisplayName("회원가입 성공: 멤버 생성 및 기본 카테고리 생성 성공")
        void signup() {
            Member member = MemberFixture.memberFixture();
            SignupRequest signupRequest = new SignupRequest(member.getName(), member.getPassword());

            Long savedId = memberService.signup(signupRequest);

            boolean existsDefaultCategory = categoryRepository.existsByNameAndMember("카테고리 없음", member);
            assertAll(
                    () -> assertThat(savedId).isEqualTo(member.getId()),
                    () -> assertThat(existsDefaultCategory).isTrue()
            );
        }

        @Test
        @DisplayName("회원가입 실패: 아이디 중복")
        void signup_fail_name_duplicate() {
            Member savedMember = memberRepository.save(MemberFixture.memberFixture());
            SignupRequest signupRequest = new SignupRequest(savedMember.getName(), savedMember.getPassword());

            assertThatThrownBy(() -> memberService.signup(signupRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessageContaining("아이디가 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("아이디 중복 검사 테스트")
    class AssertUniqueName {

        @Test
        @DisplayName("아이디 중복 검사 통과: 사용가능한 아이디")
        void assertUniqueName() {
            String name = "code";

            assertThatCode(() -> memberService.assertUniqueName(name))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("아이디 중복 검사 실패: 아이디 중복")
        void assertUniqueName_fail_duplicate() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            assertThatThrownBy(() -> memberService.assertUniqueName(member.getName()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("아이디가 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("회원 ID로 멤버 조회 테스트")
    class FindMember {

        @Test
        @DisplayName("회원 ID로 멤버 조회 성공")
        void findMember() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            FindMemberResponse actual = memberService.findMember(member, member.getId());

            assertThat(actual).isEqualTo(FindMemberResponse.from(member));
        }

        @Test
        @DisplayName("회원 ID로 멤버 조회 실패: 본인 ID가 아닌 경우")
        void findMember_Throw() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Long otherId = member.getId() + 1;

            assertThatThrownBy(() -> memberService.findMember(member, otherId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("본인의 정보만 조회할 수 있습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿을 소유한 멤버 조회 테스트")
    class GetByTemplateId {

        @Test
        @DisplayName("템플릿을 소유한 멤버 조회 성공")
        void getByTemplateId() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            Member actual = memberService.getByTemplateId(template.getId());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("템플릿을 소유한 멤버 조회 실패 : DB에 없는 템플릿인 경우")
        void getByTemplateId_Fail() {
            Long notExistsId = 100L;

            assertThatCode(() -> memberService.getByTemplateId(notExistsId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿에 대한 멤버가 존재하지 않습니다.");
        }
    }
}
