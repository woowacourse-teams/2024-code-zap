//package codezap.template.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//import java.util.List;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
//import org.springframework.transaction.annotation.Transactional;
//
//import codezap.category.domain.Category;
//import codezap.category.repository.CategoryRepository;
//import codezap.global.exception.CodeZapException;
//import codezap.template.domain.Snippet;
//import codezap.template.domain.Template;
//
//@SpringBootTest
//@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
//@Transactional
//class SnippetRepositoryTest {
//
//    @Autowired
//    private SnippetRepository snippetRepository;
//    @Autowired
//    private TemplateRepository templateRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Test
//    @DisplayName("단일 스니펫 찾기 성공: 템플릿과 순서")
//    void findOneSnippetSuccessWithTemplateAndOrdinal() {
//        Category category = categoryRepository.save(new Category("category"));
//        Template template = templateRepository.save(new Template("title", "description", category));
//        Snippet snippet1 = snippetRepository.save(new Snippet(template, "filename1", "content1", 1));
//        Snippet snippet2 = snippetRepository.save(new Snippet(template, "filename2", "content2", 2));
//
//        Snippet foundSnippet = snippetRepository.findByTemplateAndOrdinal(template, 2)
//                .orElseThrow(() -> new CodeZapException(HttpStatus.NOT_FOUND, "해당하는 스니펫이 존재하지 않습니다."));
//
//        assertAll(
//                () -> assertThat(foundSnippet.getTemplate().getTitle()).isEqualTo(template.getTitle()),
//                () -> assertThat(foundSnippet.getFilename()).isEqualTo(snippet2.getFilename()),
//                () -> assertThat(foundSnippet.getContent()).isEqualTo(snippet2.getContent()),
//                () -> assertThat(foundSnippet.getOrdinal()).isEqualTo(snippet2.getOrdinal())
//        );
//    }
//
//    @Test
//    @DisplayName("스니펫 리스트 찾기 성공: 템플릿과 순서")
//    void findSnippetsSuccessWithTemplateAndOrdinal() {
//        Category category = categoryRepository.save(new Category("category"));
//        Template template = templateRepository.save(new Template("title", "description", category));
//        Snippet snippet1 = snippetRepository.save(new Snippet(template, "filename1", "content1", 1));
//        Snippet snippet2 = snippetRepository.save(new Snippet(template, "filename2", "content2", 2));
//        Snippet snippet3 = snippetRepository.save(new Snippet(template, "filename3", "content3", 2));
//
//        List<Snippet> foundSnippets = snippetRepository.findAllByTemplateAndOrdinal(template, 2);
//
//        assertAll(
//                () -> assertThat(foundSnippets).hasSize(2),
//                () -> assertThat(foundSnippets).allMatch(snippet -> snippet.getTemplate().getId().equals(1L)),
//                () -> assertThat(foundSnippets).anyMatch(
//                        snippet -> snippet.getFilename().equals(snippet2.getFilename())),
//                () -> assertThat(foundSnippets).anyMatch(
//                        snippet -> snippet.getFilename().equals(snippet3.getFilename()))
//        );
//    }
//}