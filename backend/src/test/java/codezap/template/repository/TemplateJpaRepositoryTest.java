package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
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
    @DisplayName("카테고리 식별자로 템플릿 존재 여부 확인")
    void existsByCategoryId() {
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
}
