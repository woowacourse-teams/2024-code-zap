package codezap.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryJpaRepository;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateJpaRepository;
import codezap.template.repository.TemplateRepository;

@JpaRepositoryTest
class MemberJpaRepositoryTest {

    private final MemberRepository memberRepository;
    private final TemplateRepository templateRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    MemberJpaRepositoryTest(
            MemberJpaRepository memberRepository,
            TemplateJpaRepository templateRepository,
            CategoryJpaRepository categoryRepository
    ) {
        this.memberRepository = memberRepository;
        this.templateRepository = templateRepository;
        this.categoryRepository = categoryRepository;
    }

    @Nested
    @DisplayName("id로 Member 조회")
    class fetchById {
        @Test
        @DisplayName("성공 : id로 Member를 알아낼 수 있다.")
        void fetchByIdSuccess() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());

            Member actual = memberRepository.fetchById(member.getId());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 id인 경우 에러가 발생한다.")
        void fetchByIdFailByNotExistsId() {
            long notExistId = 100;

            assertThatThrownBy(() -> memberRepository.fetchById(notExistId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistId + "에 해당하는 멤버가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("닉네임(아이디)로 Member 조회")
    class fetchByName {
        @Test
        @DisplayName("성공 : 닉네임으로 Member를 알아낼 수 있다.")
        void fetchByNameSuccess() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());

            Member actual = memberRepository.fetchByName(member.getName());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 닉네임인 경우 에러가 발생한다.")
        void fetchByNameFailByNotExistsId() {
            String notExistName = "켬미";

            assertThatThrownBy(() -> memberRepository.fetchByName(notExistName))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("존재하지 않는 아이디 " + notExistName + " 입니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 id로 Member 조회")
    class fetchByTemplateId {
        @Test
        @DisplayName("성공 : 템플릿 id로 Member를 알아낼 수 있다.")
        void fetchByTemplateIdSuccess() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template template = new Template(member, "title", "description", category);
            template = templateRepository.save(template);

            Member actual = memberRepository.fetchByTemplateId(template.getId());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 템플릿 id인 경우 에러가 발생한다.")
        void fetchByTemplateIdFailByNotExistsId() {
            long id = 100;

            assertThatThrownBy(() -> memberRepository.fetchByTemplateId(id))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿에 대한 멤버가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("닉네임(아이디) 존재 여부")
    class existsByName {
        @Test
        @DisplayName("성공 : 해당 닉네임이 존재하면 true를 반환한다.")
        void existsByNameReturnTrue() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());

            boolean actual = memberRepository.existsByName(member.getName());

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("성공 : 해당 닉네임이 존재하지 않으면 false를 반환한다.")
        void existsByNameReturnFalse() {
            boolean actual = memberRepository.existsByName("notExist");

            assertThat(actual).isFalse();
        }
    }
}
