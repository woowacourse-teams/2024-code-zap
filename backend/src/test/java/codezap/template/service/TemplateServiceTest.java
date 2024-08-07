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
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.MemberDtoFixture;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.MemberJpaRepository;
import codezap.template.domain.Snippet;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.ThumbnailSnippet;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSnippetRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.ExploreTemplatesResponse;
import codezap.template.dto.response.FindAllMyTemplatesResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindMyTemplateResponse;
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
@Transactional
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
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    void setting() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("템플릿 생성 성공")
    void createTemplateSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        CreateTemplateRequest createTemplateRequest = makeTemplateRequest("title");

        // when
        Long id = templateService.createTemplate(createTemplateRequest, memberDto);
        Template template = templateRepository.fetchById(id);

        // then
        assertAll(
                () -> assertThat(templateRepository.findAll()).hasSize(1),
                () -> assertThat(template.getTitle()).isEqualTo(createTemplateRequest.title()),
                () -> assertThat(template.getCategory().getName()).isEqualTo("카테고리 없음")
        );
    }

    @Test
    @DisplayName("템플릿 전체 조회 성공")
    void findAllTemplatesSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberJpaRepository.fetchById(memberDto.id());
        saveTemplate(makeTemplateRequest("title1"), new Category("category1", member), member);
        saveTemplate(makeTemplateRequest("title2"), new Category("category2", member), member);

        // when
        ExploreTemplatesResponse allTemplates = templateService.findAll();

        // then
        assertThat(allTemplates.templates()).hasSize(2);
    }

    @Nested
    @DisplayName("조건에 따른 페이지 조회 메서드 동작 확인")
    class FilteringPageTest {

        private static final PageRequest DEFUALT_PAGING_REQUEST = PageRequest.of(0, 20);
        private static Category firstCategory;
        private static Tag tag1;
        private static Tag tag2;
        private static Member member;

        @BeforeEach
        void setUp() {
            //템플릿 30개 / 카테고리는 홀수 : 1번, 짝수 : 2번으로 매핑 / 태그는 16보다 작으면 1, 16 이상이면 2로 매핑
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            member = memberJpaRepository.fetchById(memberDto.id());
            firstCategory = categoryRepository.save(new Category("category1", member));
            categoryRepository.save(new Category("category2", member));
            for (int i = 1; i <= 30; i++) {
                long categoryId = i % 2 == 0 ? firstCategory.getId() + 1 : firstCategory.getId();
                templateRepository.save(new Template(
                        member,
                        "title" + i,
                        "description",
                        categoryRepository.fetchById(categoryId)
                ));

            }
            tag1 = tagRepository.save(new Tag("tag1"));
            tag2 = tagRepository.save(new Tag("tag2"));
            for (long i = 1L; i <= 30L; i++) {
                templateTagRepository.save(new TemplateTag(
                        templateRepository.fetchById(i),
                        tagRepository.fetchById((i / 16) + 1)
                ));
            }
        }

        @Test
        @DisplayName("전체 탐색 / 1페이지 성공")
        void findAllFirstPageSuccess() {
            FindAllTemplatesResponse allBy
                    = templateService.findAllBy(member.getId(), "", null, null, DEFUALT_PAGING_REQUEST);

            assertAll(
                    () -> assertThat(allBy.templates().size()).isEqualTo(20),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() <= 20),
                    () -> assertThat(allBy.totalElements()).isEqualTo(30)
            );

        }

        @Test
        @DisplayName("전체 탐색 / 2페이지 성공")
        void findAllSecondPageSuccess() {
            FindAllTemplatesResponse allBy
                    = templateService.findAllBy(member.getId(), "", null, null, PageRequest.of(1, 20));

            assertAll(
                    () -> assertThat(allBy.templates().size()).isEqualTo(10),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() > 20),
                    () -> assertThat(allBy.totalElements()).isEqualTo(30)
            );
        }

        @Test
        @DisplayName("카테고리 탐색 성공")
        void findByCategoryPageSuccess() {
            FindAllTemplatesResponse allBy
                    = templateService.findAllBy(member.getId(), "", firstCategory.getId(), null, DEFUALT_PAGING_REQUEST);

            assertAll(
                    () -> assertThat(allBy.templates().size()).isEqualTo(15),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() % 2 == 1),
                    () -> assertThat(allBy.totalElements()).isEqualTo(15)
            );
        }

        @Test
        @DisplayName("단일 태그 탐색 성공")
        void findBySingleTagPageSuccess() {
            FindAllTemplatesResponse allBy
                    = templateService.findAllBy(member.getId(), "", null, List.of(tag1.getName()), DEFUALT_PAGING_REQUEST);

            assertAll(
                    () -> assertThat(allBy.templates().size()).isEqualTo(15),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() < 16),
                    () -> assertThat(allBy.totalElements()).isEqualTo(15)
            );
        }

        @Test
        @DisplayName("복수 태그 탐색 성공")
        void findByMultipleTagPageSuccess() {
            FindAllTemplatesResponse allBy
                    = templateService.findAllBy(member.getId(), "", null, List.of(tag1.getName(), tag2.getName()), DEFUALT_PAGING_REQUEST);

            assertAll(
                    () -> assertThat(allBy.templates().size()).isEqualTo(20),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() < 21),
                    () -> assertThat(allBy.totalElements()).isEqualTo(30)
            );
        }

        @Test
        @DisplayName("카테고리 & 단일 태그 탐색 성공")
        void findByCategoryAndSingleTagPageSuccess() {
            FindAllTemplatesResponse allBy
                    = templateService.findAllBy(member.getId(), "", firstCategory.getId(), List.of(tag1.getName()), DEFUALT_PAGING_REQUEST);

            assertAll(
                    () -> assertThat(allBy.templates().size()).isEqualTo(8),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() < 16),
                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() % 2 == 1),
                    () -> assertThat(allBy.totalElements()).isEqualTo(8)
            );
        }
    }

    @Test
    @DisplayName("템플릿 단건 조회 성공")
    void findOneTemplateSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberJpaRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        FindTemplateResponse foundTemplate = templateService.findByIdAndMember(template.getId(), memberDto);

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
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberJpaRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, new Category("category1", member), member);
        categoryRepository.save(new Category("category2", member));

        // when
        UpdateTemplateRequest updateTemplateRequest = makeUpdateTemplateRequest("updateTitle");
        templateService.update(template.getId(), updateTemplateRequest, memberDto);
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
                () -> assertThat(updateTemplate.getCategory().getId()).isEqualTo(1L),
                () -> assertThat(tags).hasSize(2),
                () -> assertThat(tags.get(1).getName()).isEqualTo("tag3")
        );
    }

    @Test
    @DisplayName("템플릿 삭제 성공")
    void deleteTemplateSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberJpaRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        templateService.deleteById(1L, memberDto);

        // then
        assertAll(
                () -> assertThat(templateRepository.findAll()).isEmpty(),
                () -> assertThat(snippetRepository.findAll()).isEmpty(),
                () -> assertThat(thumbnailSnippetRepository.findAll()).isEmpty()
        );
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
                1L,
                List.of("tag1", "tag3")
        );
    }

    private Template saveTemplate(CreateTemplateRequest createTemplateRequest, Category category, Member member) {
        Category savedCategory = categoryRepository.save(category);
        Template savedTemplate = templateRepository.save(
                new Template(
                        member,
                        createTemplateRequest.title(),
                        createTemplateRequest.description(),
                        savedCategory
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
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberJpaRepository.fetchById(memberDto.id());
        Category category = categoryRepository.save(new Category("category", member));
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

    private void saveTemplateBySnippetContent(String templateTitle, String firstContent, String secondContent) {
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberJpaRepository.fetchById(memberDto.id());
        Category category = categoryRepository.save(new Category("category", member));
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

}
