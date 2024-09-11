package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Template;

@JpaRepositoryTest
class TemplateJpaRepositoryTest {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 id로 템플릿 존재 여부 확인 성공")
    void existsByCategoryId() {
        // given
        Member member = memberRepository.save(MemberFixture.getFirstMember());
        Member member2 = memberRepository.save(MemberFixture.getSecondMember());
        Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
        Category otherCategory = categoryRepository.save(CategoryFixture.getSecondCategory());
        templateRepository.save(new Template(member, "Template 1", "Description 1", category));

        assertAll(
                () -> assertThat(templateRepository.existsByCategoryId(category.getId())).isTrue(),
                () -> assertThat(templateRepository.existsByCategoryId(otherCategory.getId())).isFalse()
        );
    }

    @Nested
    @DisplayName("템플릿 id로 템플릿 조회")
    class fetchById {

        @Test
        @DisplayName("템플릿 id로 템플릿 조회 성공")
        void fetchById_W() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template savedTemplate = templateRepository.save(TemplateFixture.get(member, category));

            assertThat(templateRepository.fetchById(savedTemplate.getId())).isEqualTo(savedTemplate);
        }

        @Test
        @DisplayName("템플릿 id로 템플릿 조회 실패: 존재하지 않는 id")
        void fetchById_WhenNotExistId() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template notSavedTemplate = TemplateFixture.get(member, category);

            Long notSavedId = 1L;
            assertThatThrownBy(() -> templateRepository.fetchById(notSavedId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notSavedId + "에 해당하는 템플릿이 존재하지 않습니다.");
        }
    }


}
