package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.template.domain.Snippet;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.ThumbnailSnippet;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSnippetRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.repository.SnippetRepository;
import codezap.template.repository.TagRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateTagRepository;
import codezap.template.repository.ThumbnailSnippetRepository;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
class TemplateServiceTest {

    @LocalServerPort
    int port;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private SnippetRepository snippetRepository;

    @Autowired
    private ThumbnailSnippetRepository thumbnailSnippetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TemplateTagRepository templateTagRepository;
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setting() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("템플릿 생성 성공")
    void createTemplateSuccess() {
        // given
        categoryRepository.save(new Category("category"));
        CreateTemplateRequest createTemplateRequest = makeTemplateRequest("title");

        // when
        Long id = templateService.create(createTemplateRequest);
        Template template = templateRepository.fetchById(id);

        // then
        assertAll(
                () -> assertThat(templateRepository.findAll()).hasSize(1),
                () -> assertThat(template.getTitle()).isEqualTo(createTemplateRequest.title()),
                () -> assertThat(template.getCategory().getName()).isEqualTo("category")
        );
    }

    @Test
    @DisplayName("템플릿 전체 조회 성공")
    void findAllTemplatesSuccess() {
        // given
        saveTemplate(makeTemplateRequest("title1"));
        saveTemplate(makeTemplateRequest("title2"));

        // when
        FindAllTemplatesResponse allTemplates = templateService.findAll();

        // then
        assertThat(allTemplates.templates()).hasSize(2);
    }

    @Test
    @DisplayName("템플릿 단건 조회 성공")
    void findOneTemplateSuccess() {
        // given
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate);

        // when
        FindTemplateResponse foundTemplate = templateService.findById(template.getId());

        // then
        assertAll(
                () -> assertThat(foundTemplate.title()).isEqualTo(template.getTitle()),
                () -> assertThat(foundTemplate.snippets()).hasSize(
                        snippetRepository.findAllByTemplate(template).size()),
                () -> assertThat(foundTemplate.category().id()).isEqualTo(template.getCategory().getId()),
                () -> assertThat(foundTemplate.tags()).hasSize(2)
        );
    }

    @Test
    @DisplayName("템플릿 수정 성공")
    void updateTemplateSuccess() {
        // given
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate);
        categoryRepository.save(new Category("category2"));

        // when
        UpdateTemplateRequest updateTemplateRequest = makeUpdateTemplateRequest("updateTitle");
        templateService.update(template.getId(), updateTemplateRequest);
        Template updateTemplate = templateRepository.fetchById(template.getId());
        List<Snippet> snippets = snippetRepository.findAllByTemplate(template);
        ThumbnailSnippet thumbnailSnippet = thumbnailSnippetRepository.findById(template.getId()).get();
        List<Tag> tags = templateTagRepository.findAllByTemplate(updateTemplate).stream()
                .map(TemplateTag::getTag)
                .toList();

        // then
        assertAll(
                () -> assertThat(updateTemplate.getTitle()).isEqualTo("updateTitle"),
                () -> assertThat(thumbnailSnippet.getSnippet().getId()).isEqualTo(2L),
                () -> assertThat(snippets).hasSize(3),
                () -> assertThat(updateTemplate.getCategory().getId()).isEqualTo(2L),
                () -> assertThat(tags).hasSize(2),
                () -> assertThat(tags.get(1).getName()).isEqualTo("tag3")
        );
    }

    @Test
    @DisplayName("템플릿 삭제 성공")
    void deleteTemplateSuccess() {
        // given
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        saveTemplate(createdTemplate);

        // when
        templateService.deleteById(1L);

        // then
        assertAll(
                () -> assertThat(templateRepository.findAll()).isEmpty(),
                () -> assertThat(snippetRepository.findAll()).isEmpty(),
                () -> assertThat(thumbnailSnippetRepository.findAll()).isEmpty()
        );
    }

    @Test
    @DisplayName("템플릿 토픽 검색 성공 : 템플릿 제목에 포함")
    void findAllTemplatesTitleContainTopicSuccess() {
        //given
        saveTemplate(makeTemplateRequest("hello"));
        saveTemplate(makeTemplateRequest("hello topic"));
        saveTemplate(makeTemplateRequest("topic hello"));
        saveTemplate(makeTemplateRequest("hello topic !"));

        //when
        FindAllTemplatesResponse templates = templateService.findContainTopic("topic");

        //then
        assertThat(templates.templates()).hasSize(3);
    }

    @Test
    @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 내에 스니펫 파일명 중 하나라도 포함")
    void findAllSnippetFilenameContainTopicSuccess() {
        //given
        saveTemplateBySnippetFilename("tempate1", "login.js", "signup.js");
        saveTemplateBySnippetFilename("tempate2", "login.java", "signup.java");
        saveTemplateBySnippetFilename("tempate3", "login.js", "signup.java");

        //when
        FindAllTemplatesResponse templates = templateService.findContainTopic("java");

        //then
        assertThat(templates.templates()).hasSize(2);
    }

    private CreateTemplateRequest makeTemplateRequest(String title) {
        return new CreateTemplateRequest(
                title,
                "description",
                List.of(
                        new CreateSnippetRequest("filename1", "content1", 1),
                        new CreateSnippetRequest("filename2", "content2", 2)
                ),
                1L,
                List.of("tag1", "tag2")
        );
    }

    private UpdateTemplateRequest makeUpdateTemplateRequest(String title) {
        return new UpdateTemplateRequest(
                title,
                "description",
                List.of(
                        new CreateSnippetRequest("filename3", "content3", 2),
                        new CreateSnippetRequest("filename4", "content4", 3)
                ),
                List.of(
                        new UpdateSnippetRequest(2L, "filename2", "content2", 1)
                ),
                List.of(1L),
                2L,
                List.of("tag1", "tag3")
        );
    }

    private Template saveTemplate(CreateTemplateRequest createTemplateRequest) {
        Category category = categoryRepository.save(new Category("category"));
        Template savedTemplate = templateRepository.save(
                new Template(
                        createTemplateRequest.title(),
                        createTemplateRequest.description(),
                        category
                )
        );
        Snippet savedFirstSnippet = snippetRepository.save(new Snippet(savedTemplate, "filename1", "content1", 1));
        snippetRepository.save(new Snippet(savedTemplate, "filename2", "content2", 2));
        thumbnailSnippetRepository.save(new ThumbnailSnippet(savedTemplate, savedFirstSnippet));
        createTemplateRequest.tags().stream()
                .map(Tag::new)
                .map(tagRepository::save)
                .forEach(tag -> templateTagRepository.save(new TemplateTag(savedTemplate, tag)));

        return savedTemplate;
    }

    private void saveTemplateBySnippetFilename(String templateTitle, String firstFilename, String secondFilename) {
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest(
                templateTitle,
                List.of(
                        new CreateSnippetRequest(firstFilename, "content1", 1),
                        new CreateSnippetRequest(secondFilename, "content2", 2)
                )
        );
        Template savedTemplate = templateRepository.save(new Template(createTemplateRequest.title()));

        Snippet savedFirstSnippet = snippetRepository.save(new Snippet(savedTemplate, firstFilename, "content1", 1));
        snippetRepository.save(new Snippet(savedTemplate, secondFilename, "content2", 2));
        thumbnailSnippetRepository.save(new ThumbnailSnippet(savedTemplate, savedFirstSnippet));
    }
}
