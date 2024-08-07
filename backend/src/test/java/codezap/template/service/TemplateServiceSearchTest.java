package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import codezap.fixture.MemberDtoFixture;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Snippet;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.ThumbnailSnippet;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindAllTemplatesResponse.ItemResponse;
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
        return new CreateTemplateRequest(title, "description",
                List.of(new CreateSnippetRequest("filename1", "content1", 1),
                        new CreateSnippetRequest("filename2", "content2", 2)), 1L, List.of("tag1", "tag2"));
    }

    private void saveDefault15Templates(Member member, Category category) {
        saveTemplate(makeTemplateRequest("hello keyword 1"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 2"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 3"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 4"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 5"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 6"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 7"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 8"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 9"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 10"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 11"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 12"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 13"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 14"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 15"), member, category);

        tagRepository.save(new Tag("tag1"));
        tagRepository.save(new Tag("tag2"));
    }

    private Template saveTemplate(CreateTemplateRequest createTemplateRequest, Member member, Category category) {
        Template savedTemplate = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category));
        Snippet savedFirstSnippet = snippetRepository.save(new Snippet(savedTemplate, "filename1", "content1", 1));
        snippetRepository.save(new Snippet(savedTemplate, "filename2", "content2", 2));
        thumbnailSnippetRepository.save(new ThumbnailSnippet(savedTemplate, savedFirstSnippet));

        return savedTemplate;
    }

    private void saveTemplateBySnippetFilename(String templateTitle, String firstFilename, String secondFilename,
            Member member, Category category
    ) {
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest(templateTitle, "설명",
                List.of(new CreateSnippetRequest(firstFilename, "content1", 1),
                        new CreateSnippetRequest(secondFilename, "content2", 2)), category.getId(), List.of());
        Template savedTemplate = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category));

        Snippet savedFirstSnippet = snippetRepository.save(new Snippet(savedTemplate, firstFilename, "content1", 1));
        snippetRepository.save(new Snippet(savedTemplate, secondFilename, "content2", 2));
        thumbnailSnippetRepository.save(new ThumbnailSnippet(savedTemplate, savedFirstSnippet));
    }

    private void saveTemplateBySnippetContent(String templateTitle, String firstContent, String secondContent,
            Member member, Category category
    ) {
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest(templateTitle, "설명",
                List.of(new CreateSnippetRequest("filename1", firstContent, 1),
                        new CreateSnippetRequest("filename2", secondContent, 2)), category.getId(), List.of());
        Template savedTemplate = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category));

        Snippet savedFirstSnippet = snippetRepository.save(new Snippet(savedTemplate, "filename1", firstContent, 1));
        snippetRepository.save(new Snippet(savedTemplate, "filename2", secondContent, 2));
        thumbnailSnippetRepository.save(new ThumbnailSnippet(savedTemplate, savedFirstSnippet));
    }

    @Nested
    @DisplayName("템플릿 토픽 검색")
    class searchContainKeyword {
        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 템플릿 제목에 포함")
        void findAllTemplatesTitleContainKeywordSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            saveTemplate(makeTemplateRequest("hello"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword"), member, category);
            saveTemplate(makeTemplateRequest("keyword hello"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword !"), member, category);

            //when
            FindAllTemplatesResponse templates = templateService.findAllBy(
                    member.getId(), "keyword", null, null, PageRequest.of(1, 3)
            );

            //then
            assertThat(templates.templates()).hasSize(3);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 내에 스니펫 파일명 중 하나라도 포함")
        void findAllSnippetFilenameContainKeywordSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            saveTemplateBySnippetFilename("tempate1", "login.js", "signup.js", member, category);
            saveTemplateBySnippetFilename("tempate2", "login.java", "signup.java", member, category);
            saveTemplateBySnippetFilename("tempate3", "login.js", "signup.java", member, category);

            //when
            FindAllTemplatesResponse templates = templateService.findAllBy(
                    member.getId(), "java", null, null, PageRequest.of(1, 3)
            );

            //then
            assertThat(templates.templates()).hasSize(2);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 내에 스니펫 코드 중 하나라도 포함")
        void findAllSnippetContentContainKeywordSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            saveTemplateBySnippetContent("tempate1", "public Main {", "new Car();", member, category);
            saveTemplateBySnippetContent("tempate2", "private Car", "public Movement", member, category);
            saveTemplateBySnippetContent("tempate3", "console.log", "a+b=3", member, category);

            //when
            FindAllTemplatesResponse templates = templateService.findAllBy(
                    member.getId(), "Car", null, null, PageRequest.of(1, 3)
            );

            //then
            assertThat(templates.templates()).hasSize(2);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 설명에 포함")
        void findAllDescriptionContainKeywordSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            CreateTemplateRequest request1 = new CreateTemplateRequest("타이틀", "Login 구현",
                    List.of(new CreateSnippetRequest("filename1", "content1", 1),
                            new CreateSnippetRequest("filename2", "content2", 2)), category.getId(),
                    List.of("tag1", "tag2"));
            saveTemplate(request1, member, category);
            CreateTemplateRequest request2 = new CreateTemplateRequest("타이틀", "Signup 구현",
                    List.of(new CreateSnippetRequest("filename1", "content1", 1),
                            new CreateSnippetRequest("filename2", "content2", 2)), category.getId(),
                    List.of("tag1", "tag2"));
            saveTemplate(request2, member, category);

            //when
            FindAllTemplatesResponse templates = templateService.findAllBy(
                    member.getId(), "Login", null, null, PageRequest.of(1, 3)
            );

            //then
            assertThat(templates.templates()).hasSize(1);
        }

        @Test
        @DisplayName("전체 탐색 / 1페이지 성공")
        void findAllFirstPageSuccess() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);
            FindAllTemplatesResponse allBy = templateService.findAllBy(
                    member.getId(), "", null, null, PageRequest.of(1, 20)
            );

            assertAll(() -> assertThat(allBy.templates()).hasSize(20),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() <= 20),
                    () -> assertThat(allBy.totalElements()).isEqualTo(30));
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 페이징 성공")
        void findAllContainKeywordPaging() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            saveDefault15Templates(member, category);

            //when
            FindAllTemplatesResponse templates = templateService.findAllBy(
                    member.getId(), "keyword", null, null, PageRequest.of(2, 5)
            );

            //then
            List<String> titles = templates.templates().stream().map(ItemResponse::title).toList();
            assertThat(titles).containsExactly("hello keyword 6", "hello keyword 7", "hello keyword 8", "hello keyword 9",
                    "hello keyword 10");
        }
    }

    @Nested
    @DisplayName("조건에 따른 페이지 조회 메서드 동작 확인")
    class FilteringPageTest {

        private static final PageRequest DEFAULT_PAGING_REQUEST = PageRequest.of(1, 20);

        @Test
        @DisplayName("전체 탐색 / 1페이지 성공")
        void findAllFirstPageSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            FindAllTemplatesResponse allBy = templateService.findAllBy(
                    member.getId(), "", null, null, PageRequest.of(1, 20)
            );

            //when & then
            assertAll(() -> assertThat(allBy.templates()).hasSize(20),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() <= 20),
                    () -> assertThat(allBy.totalElements()).isEqualTo(30));
        }

        @Test
        @DisplayName("전체 탐색 / 2페이지 성공")
        void findAllSecondPageSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            FindAllTemplatesResponse allBy = templateService.findAllBy(
                    member.getId(), "", null, null, PageRequest.of(2, 20)
            );

            assertAll(() -> assertThat(allBy.templates()).hasSize(10),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() > 20),
                    () -> assertThat(allBy.totalElements()).isEqualTo(30));
        }

        @Test
        @DisplayName("카테고리 탐색 성공")
        void findByCategoryPageSuccess() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            FindAllTemplatesResponse allBy = templateService.findAllBy(
                    member.getId(), "", category1.getId(), null, PageRequest.of(1, 20)
            );

            assertAll(() -> assertThat(allBy.templates()).hasSize(15),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() <= 15),
                    () -> assertThat(allBy.totalElements()).isEqualTo(15));
        }

        @Test
        @DisplayName("단일 태그 탐색 성공")
        void findBySingleTagPageSuccess() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);
            for (long i = 1L; i <= 30L; i++) {
                templateTagRepository.save(
                        new TemplateTag(templateRepository.fetchById(i), tagRepository.fetchById((i % 2) + 1)));
            }
            FindAllTemplatesResponse allBy = templateService.findAllBy(
                    member.getId(), "", null, List.of("tag1"), DEFAULT_PAGING_REQUEST
            );

            assertAll(() -> assertThat(allBy.templates()).hasSize(15),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() % 2 == 0), // 테그
                    () -> assertThat(allBy.totalElements()).isEqualTo(15));
        }

        @Test
        @DisplayName("복수 태그 탐색 성공")
        void findByMultipleTagPageSuccess() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);
            for (long i = 1L; i <= 30L; i++) {
                templateTagRepository.save(
                        new TemplateTag(templateRepository.fetchById(i), tagRepository.fetchById((i / 15) + 1)));
            }

            FindAllTemplatesResponse allBy = templateService.findAllBy(
                    member.getId(), "", null, List.of("tag1", "tag2"), DEFAULT_PAGING_REQUEST
            );

            assertAll(() -> assertThat(allBy.templates()).hasSize(20),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() < 21),
                    () -> assertThat(allBy.totalElements()).isEqualTo(30));
        }

        @Test
        @DisplayName("카테고리 & 단일 태그 탐색 성공")
        void findByCategoryAndSingleTagPageSuccess() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);
            for (long i = 1L; i <= 30L; i++) {
                templateTagRepository.save(
                        new TemplateTag(templateRepository.fetchById(i), tagRepository.fetchById((i % 2) + 1)));
            }
            FindAllTemplatesResponse allBy = templateService.findAllBy(
                    member.getId(), "", category1.getId(), List.of("tag1"), DEFAULT_PAGING_REQUEST
            );

            assertAll(() -> assertThat(allBy.templates()).hasSize(7),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() < 16),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() % 2 == 0),
                    () -> assertThat(allBy.totalElements()).isEqualTo(7));
        }
    }
}
