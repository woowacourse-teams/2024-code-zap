package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Snippet;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.ThumbnailSnippet;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.response.FindAllMyTemplatesResponse;
import codezap.template.dto.response.FindMyTemplateResponse;
import codezap.template.repository.SnippetRepository;
import codezap.template.repository.TagRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateTagRepository;
import codezap.template.repository.ThumbnailSnippetRepository;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
class TemplateServiceSearchTest {

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

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setting() {
        RestAssured.port = port;
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

    private Template saveTemplate(CreateTemplateRequest createTemplateRequest, Member member, Category category) {
        Template savedTemplate = templateRepository.save(
                new Template(
                        member,
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

    private void saveTemplateBySnippetFilename(
            String templateTitle, String firstFilename, String secondFilename, Member member, Category category
    ) {
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest(
                templateTitle, "설명",
                List.of(
                        new CreateSnippetRequest(firstFilename, "content1", 1),
                        new CreateSnippetRequest(secondFilename, "content2", 2)
                ),
                category.getId(),
                List.of()
        );
        Template savedTemplate = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category));

        Snippet savedFirstSnippet = snippetRepository.save(new Snippet(savedTemplate, firstFilename, "content1", 1));
        snippetRepository.save(new Snippet(savedTemplate, secondFilename, "content2", 2));
        thumbnailSnippetRepository.save(new ThumbnailSnippet(savedTemplate, savedFirstSnippet));
    }

    private void saveTemplateBySnippetContent(
            String templateTitle, String firstContent, String secondContent, Member member, Category category
    ) {
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest(
                templateTitle, "설명",
                List.of(
                        new CreateSnippetRequest("filename1", firstContent, 1),
                        new CreateSnippetRequest("filename2", secondContent, 2)
                ),
                category.getId(),
                List.of()
        );
        Template savedTemplate = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category));

        Snippet savedFirstSnippet = snippetRepository.save(new Snippet(savedTemplate, "filename1", firstContent, 1));
        snippetRepository.save(new Snippet(savedTemplate, "filename2", secondContent, 2));
        thumbnailSnippetRepository.save(new ThumbnailSnippet(savedTemplate, savedFirstSnippet));
    }

    @Nested
    @DisplayName("템플릿 토픽 검색")
    class searchContainTopic {
        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 템플릿 제목에 포함")
        void findAllTemplatesTitleContainTopicSuccess() {
            //given
            Member member = memberRepository.save(new Member("a@a.com", "aaa1", "kyummi"));
            Category category = categoryRepository.save(new Category("category"));
            saveTemplate(makeTemplateRequest("hello"), member, category);
            saveTemplate(makeTemplateRequest("hello topic"), member, category);
            saveTemplate(makeTemplateRequest("topic hello"), member, category);
            saveTemplate(makeTemplateRequest("hello topic !"), member, category);

            //when
            FindAllMyTemplatesResponse templates = templateService.findContainTopic(member.getId(), "topic",
                    PageRequest.of(1, 3));

            //then
            assertThat(templates.templates()).hasSize(3);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 내에 스니펫 파일명 중 하나라도 포함")
        void findAllSnippetFilenameContainTopicSuccess() {
            //given
            Member member = memberRepository.save(new Member("a@a.com", "aaa1", "kyummi"));
            Category category = categoryRepository.save(new Category("category"));
            saveTemplateBySnippetFilename("tempate1", "login.js", "signup.js", member, category);
            saveTemplateBySnippetFilename("tempate2", "login.java", "signup.java", member, category);
            saveTemplateBySnippetFilename("tempate3", "login.js", "signup.java", member, category);

            //when
            FindAllMyTemplatesResponse templates = templateService.findContainTopic(member.getId(), "java",
                    PageRequest.of(1, 2));

            //then
            assertThat(templates.templates()).hasSize(2);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 내에 스니펫 코드 중 하나라도 포함")
        void findAllSnippetContentContainTopicSuccess() {
            //given
            Member member = memberRepository.save(new Member("a@a.com", "aaa1", "kyummi"));
            Category category = categoryRepository.save(new Category("category"));
            saveTemplateBySnippetContent("tempate1", "public Main {", "new Car();", member, category);
            saveTemplateBySnippetContent("tempate2", "private Car", "public Movement", member, category);
            saveTemplateBySnippetContent("tempate3", "console.log", "a+b=3", member, category);

            //when
            FindAllMyTemplatesResponse templates = templateService.findContainTopic(member.getId(), "Car",
                    PageRequest.of(1, 2));

            //then
            assertThat(templates.templates()).hasSize(2);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 설명에 포함")
        void findAllDescriptionContainTopicSuccess() {
            //given
            Member member = memberRepository.save(new Member("a@a.com", "aaa1", "kyummi"));
            Category category = categoryRepository.save(new Category("category"));
            CreateTemplateRequest request1 = new CreateTemplateRequest(
                    "타이틀",
                    "Login 구현",
                    List.of(
                            new CreateSnippetRequest("filename1", "content1", 1),
                            new CreateSnippetRequest("filename2", "content2", 2)
                    ),
                    category.getId(),
                    List.of("tag1", "tag2")
            );
            saveTemplate(request1, member, category);
            CreateTemplateRequest request2 = new CreateTemplateRequest(
                    "타이틀",
                    "Signup 구현",
                    List.of(
                            new CreateSnippetRequest("filename1", "content1", 1),
                            new CreateSnippetRequest("filename2", "content2", 2)
                    ),
                    category.getId(),
                    List.of("tag1", "tag2")
            );
            saveTemplate(request2, member, category);

            //when
            FindAllMyTemplatesResponse templates = templateService.findContainTopic(member.getId(), "Login",
                    PageRequest.of(1, 1));

            //then
            assertThat(templates.templates()).hasSize(1);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 페이징 성공")
        void findAllContainTopicPaging() {
            //given
            Member member = memberRepository.save(new Member("a@a.com", "aaa1", "kyummi"));
            Category category = categoryRepository.save(new Category("category"));
            saveTemplate(makeTemplateRequest("hello topic 1"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 2"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 3"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 4"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 5"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 6"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 7"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 8"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 9"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 10"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 11"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 12"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 13"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 14"), member, category);
            saveTemplate(makeTemplateRequest("hello topic 15"), member, category);

            //when
            FindAllMyTemplatesResponse templates = templateService.findContainTopic(member.getId(), "topic",
                    PageRequest.of(2, 5));

            //then
            List<String> titles = templates.templates().stream().map(FindMyTemplateResponse::title).toList();
            assertThat(titles).containsExactly(
                    "hello topic 6", "hello topic 7", "hello topic 8", "hello topic 9", "hello topic 10");
        }
    }
}
