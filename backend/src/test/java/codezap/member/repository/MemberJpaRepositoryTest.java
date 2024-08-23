package codezap.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryJpaRepository;
import codezap.fixture.CategoryFixture;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateJpaRepository;

@JpaRepositoryTest
class MemberJpaRepositoryTest {

    private final MemberJpaRepository memberJpaRepository;
    private final TemplateJpaRepository templateJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Autowired
    MemberJpaRepositoryTest(MemberJpaRepository memberJpaRepository, TemplateJpaRepository templateJpaRepository,
            CategoryJpaRepository categoryJpaRepository
    ) {
        this.memberJpaRepository = memberJpaRepository;
        this.templateJpaRepository = templateJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Test
    void fetchByTemplateId() {
        // Then
        Member member = ((JpaRepository<Member, Long>)memberJpaRepository).save(MemberFixture.memberFixture());
        Category category = ((JpaRepository<Category, Long>)categoryJpaRepository).save(CategoryFixture.getFirstCategory());
        Template template = new Template(member, "title", "description", category);
        Template savedTemplate = ((JpaRepository<Template, Long>)templateJpaRepository).save(template);

        assertThat(memberJpaRepository.fetchByTemplateId(savedTemplate.getId())).isEqualTo(member);
    }
}
