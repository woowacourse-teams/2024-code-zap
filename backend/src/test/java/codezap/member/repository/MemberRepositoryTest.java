package codezap.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.RepositoryTest;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateRepository;

@RepositoryTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Nested
    @DisplayName("id로 Member 조회")
    class FetchById {

        @Test
        @DisplayName("성공 : id로 Member 조회 가능")
        void fetchByIdSuccess() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());

            Member actual = memberRepository.fetchById(member.getId());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 id인 경우 예외 발생")
        void fetchByIdFailByNotExistsId() {
            long notExistId = 100;

            assertThatThrownBy(() -> memberRepository.fetchById(notExistId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistId + "에 해당하는 멤버가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("닉네임(아이디)로 Member 조회")
    class FetchByName {

        @Test
        @DisplayName("성공 : 닉네임으로 Member 조회 가능")
        void fetchByNameSuccess() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());

            Member actual = memberRepository.fetchByName(member.getName());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 닉네임인 경우 예외 발생")
        void fetchByNameFailByNotExistsId() {
            String notExistName = "켬미";

            assertThatThrownBy(() -> memberRepository.fetchByName(notExistName))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("존재하지 않는 아이디 " + notExistName + " 입니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 id로 Member 조회")
    class FetchByTemplateId {

        @Test
        @DisplayName("성공 : 템플릿 id로 Member 조회 가능")
        void fetchByTemplateIdSuccess() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template template = templateRepository.save(new Template(member, "title", "description", category));

            Member actual = memberRepository.fetchByTemplateId(template.getId());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 템플릿 id인 경우 예외 발생")
        void fetchByTemplateIdFailByNotExistsId() {
            long notExistId = 100;

            assertThatThrownBy(() -> memberRepository.fetchByTemplateId(notExistId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿에 대한 멤버가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("닉네임(아이디) 존재 여부")
    class ExistsByName {

        @Test
        @DisplayName("성공 : 해당 닉네임이 존재하면 true를 반환")
        void existsByNameReturnTrue() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());

            boolean actual = memberRepository.existsByName(member.getName());

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("성공 : 해당 닉네임이 존재하지 않으면 false를 반환")
        void existsByNameReturnFalse() {
            boolean actual = memberRepository.existsByName("notExist");

            assertThat(actual).isFalse();
        }
    }
}
