package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;

@JpaRepositoryTest
public class SourceCodeRepositoryTest {

    @Autowired
    private SourceCodeRepository sourceCodeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TemplateRepository templateRepository;

    private Member member1;
    private Template template1;
    private Category category1;
    private SourceCode sourceCode1, sourceCode2, sourceCode3;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("user1@test.com", "pp", "salt1"));

        category1 = categoryRepository.save(new Category("Category 1", member1));

        template1 = templateRepository.save(new Template(member1, "Template 1", "Description 1", category1));

        sourceCode1 = new SourceCode(template1, "SourceCode 1", "Content 1", 1);
        sourceCode2 = new SourceCode(template1, "SourceCode 2", "Content 2", 2);
        sourceCodeRepository.save(sourceCode1);
        sourceCodeRepository.save(sourceCode2);
    }

    @Test
    @DisplayName("소스코드 ID로 소스코드 조회")
    void testFindBySourceCodeId() {
        var result = sourceCodeRepository.fetchById(1L);

        assertThat(result.getFilename()).isEqualTo("SourceCode 1");
    }

    @Test
    @DisplayName("템플릿으로 전체 소스코드 조회")
    void testFindAllByTemplate() {
        var result = sourceCodeRepository.findAllByTemplate(template1);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFilename()).isEqualTo("SourceCode 1");
    }

    @Test
    @DisplayName("템플릿과 순서로 소스코드 조회")
    void testFindByTemplateAndOrdinal() {
        var result = sourceCodeRepository.fetchByTemplateAndOrdinal(template1, 2);

        assertThat(result.getFilename()).isEqualTo("SourceCode 2");
    }

    @Test
    @DisplayName("템플릿과 순서에 해당하는 전체 소스코드 조회")
    void testFindAllByTemplateAndOrdinal() {
        var sut = new SourceCode(template1, "SourceCode 4", "Content 4", 1);
        sourceCodeRepository.save(sut);

        var result = sourceCodeRepository.findAllByTemplateAndOrdinal(template1, 1);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("템플릿에 존재하는 소스코드 개수 조회")
    void testCountByTemplate() {
        var result = sourceCodeRepository.countByTemplate(template1);

        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("템플릿 ID로 템플릿에 존재하는 소스코드 삭제")
    void testDeleteByTemplateId() {
        sourceCodeRepository.deleteByTemplateId(1L);

        var result = sourceCodeRepository.findAllByTemplate(template1);

        assertThat(result).hasSize(0);
    }
}
