package codezap.template.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.category.service.CategoryService;
import codezap.fixture.MemberDtoFixture;
import codezap.global.exception.GlobalExceptionHandler;
import codezap.member.configuration.AuthArgumentResolver;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.member.service.AuthService;
import codezap.member.service.MemberService;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSnippetRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.FakeSnippetRepository;
import codezap.template.repository.FakeTagRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.FakeTemplateTagRepository;
import codezap.template.repository.FakeThumbnailSnippetRepository;
import codezap.template.repository.SnippetRepository;
import codezap.template.repository.TagRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateTagRepository;
import codezap.template.repository.ThumbnailSnippetRepository;
import codezap.template.service.TemplateApplicationService;
import codezap.template.service.TemplateService;

class TemplateControllerTest {

    private static final int MAX_LENGTH = 255;

    private Member firstMember = new Member(1L, "test1@email.com", "password1234", "username1");
    private Member secondMember = new Member(2L, "test2@email.com", "password1234", "username2");
    private Category firstCategory = new Category(1L, firstMember, "카테고리 없음", true);
    private Category secondCategory = new Category(2L, secondMember, "카테고리 없음", true);

    private final TemplateRepository templateRepository = new FakeTemplateRepository();
    private final SnippetRepository snippetRepository = new FakeSnippetRepository();
    private final ThumbnailSnippetRepository thumbnailSnippetRepository = new FakeThumbnailSnippetRepository();
    private final CategoryRepository categoryRepository = new FakeCategoryRepository(List.of(firstCategory, secondCategory));
    private final TemplateTagRepository templateTagRepository = new FakeTemplateTagRepository();
    private final TagRepository tagRepository = new FakeTagRepository();
    private final MemberRepository memberRepository = new FakeMemberRepository(List.of(firstMember, secondMember));
    private final TemplateService templateService = new TemplateService(
            thumbnailSnippetRepository,
            templateRepository,
            snippetRepository,
            categoryRepository,
            tagRepository,
            templateTagRepository,
            memberRepository);
    private final CategoryService categoryService = new CategoryService(categoryRepository, templateRepository,
            memberRepository);
    private final AuthService authService = new AuthService(memberRepository);

    private final MemberService memberService = new MemberService(memberRepository, authService, categoryRepository);

    private final TemplateApplicationService applicationService = new TemplateApplicationService(memberService,
            templateService);

    private final TemplateController templateController = new TemplateController(templateService, applicationService);

    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(templateController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new AuthArgumentResolver(authService),
                    new PageableHandlerMethodArgumentResolver())
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    Cookie cookie;

    @BeforeEach
    void setCookie() {
        String basicAuth = HttpHeaders.encodeBasicAuth(firstMember.getEmail(), firstMember.getPassword(), StandardCharsets.UTF_8);
        cookie = new Cookie("Authorization", basicAuth);
    }

    @Nested
    @DisplayName("템플릿 생성 테스트")
    class createTemplateTest {

        @ParameterizedTest
        @DisplayName("템플릿 생성 성공")
        @CsvSource({"a, 65535", "ㄱ, 21845"})
        void createTemplateSuccess(String repeatTarget, int maxLength) throws Exception {
            String maxTitle = "a".repeat(MAX_LENGTH);
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    maxTitle,
                    repeatTarget.repeat(maxLength),
                    List.of(new CreateSnippetRequest("a".repeat(MAX_LENGTH), repeatTarget.repeat(maxLength), 1)),
                    1,
                    1L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("템플릿 생성 실패: 로그인을 하지 않은 유저")
        void createTemplateFailWithNotLogin() throws Exception {
            String maxTitle = "title";
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    maxTitle,
                    "description",
                    List.of(new CreateSnippetRequest("filename", "content", 1)),
                    1,
                    1L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("템플릿 생성 실패: 템플릿 이름 길이 초과")
        void createTemplateFailWithLongTitle() throws Exception {
            String exceededTitle = "a".repeat(MAX_LENGTH + 1);
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    exceededTitle,
                    "description",
                    List.of(new CreateSnippetRequest("a", "content", 1)),
                    1,
                    1L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("템플릿명은 최대 255자까지 입력 가능합니다."));
        }

        @Test
        @DisplayName("템플릿 생성 실패: 파일 이름 길이 초과")
        void createTemplateFailWithLongFileName() throws Exception {
            String exceededTitle = "a".repeat(MAX_LENGTH + 1);
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSnippetRequest(exceededTitle, "content", 1)),
                    1,
                    1L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("파일명은 최대 255자까지 입력 가능합니다."));
        }

        @ParameterizedTest
        @DisplayName("템플릿 생성 실패: 파일 내용 길이 초과")
        @CsvSource({"a, 65536", "ㄱ, 21846"})
        void createTemplateFailWithLongContent(String repeatTarget, int exceededLength) throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSnippetRequest("title", repeatTarget.repeat(exceededLength), 1)),
                    1,
                    1L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("소스 코드는 최대 65,535 Byte까지 입력 가능합니다."));
        }

        @ParameterizedTest
        @DisplayName("템플릿 생성 실패: 템플릿 설명 길이 초과")
        @CsvSource({"a, 65536", "ㄱ, 21846"})
        void createTemplateFailWithLongDescription(String repeatTarget, int exceededLength) throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    repeatTarget.repeat(exceededLength),
                    List.of(new CreateSnippetRequest("title", "content", 1)),
                    1,
                    1L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("템플릿 설명은 최대 65,535 Byte까지 입력 가능합니다."));
        }

        @ParameterizedTest
        @DisplayName("템플릿 생성 실패: 잘못된 스니펫 순서 입력")
        @CsvSource({"0, 1", "1, 3", "2, 1"})
        void createTemplateFailWithWrongSnippetOrdinal(int firstIndex, int secondIndex) throws Exception {
            MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSnippetRequest("title", "content", firstIndex),
                            new CreateSnippetRequest("title", "content", secondIndex)),
                    1,
                    1L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("스니펫 순서가 잘못되었습니다."));
        }
    }

    @Nested
    @DisplayName("템플릿 검색 테스트")
    class getTemplatesTest {

        @Test
        @DisplayName("템플릿 검색 성공")
        void findAllTemplatesSuccess() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSnippets("title1");
            CreateTemplateRequest templateRequest2 = createTemplateRequestWithTwoSnippets("title2");
            templateService.createTemplate(templateRequest1, memberDto);
            templateService.createTemplate(templateRequest2, memberDto);

            // when & then
            mvc.perform(get("/templates")
                            .cookie(cookie)
                            .param("memberId", "1")
                            .param("keyword", "")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.templates.size()").value(2));
        }

        @Test
        @DisplayName("템플릿 검색 실패: 태그 ID가 0개인 경우")
        void findAllTemplatesFailWithZeroTagIds() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSnippets("title1");
            templateService.createTemplate(templateRequest1, memberDto);

            // when & then
            mvc.perform(get("/templates")
                            .cookie(cookie)
                            .param("memberId", "1")
                            .param("keyword", "")
                            .param("tagIds", "")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("태그 ID가 0개입니다. 필터링 하지 않을 경우 null로 전달해주세요."));
        }
    }

    @Nested
    @DisplayName("템플릿 상세 조회 테스트")
    class findTemplateTest {

        @Test
        @DisplayName("템플릿 상세 조회 성공")
        void findOneTemplateSuccess() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSnippets("title");
            templateService.createTemplate(templateRequest, memberDto);

            // when & then
            mvc.perform(get("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(templateRequest.title()))
                    .andExpect(jsonPath("$.snippets.size()").value(2))
                    .andExpect(jsonPath("$.category.id").value(1))
                    .andExpect(jsonPath("$.category.name").value("카테고리 없음"))
                    .andExpect(jsonPath("$.tags.size()").value(2));
        }

        @Test
        @DisplayName("템플릿 상세 조회 실패: 존재하지 않는 템플릿 조회")
        void findOneTemplateFailWithNotFoundTemplate() throws Exception {
            // when & then
            mvc.perform(get("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("식별자 1에 해당하는 템플릿이 존재하지 않습니다."));
        }

        @Test
        @DisplayName("템플릿 상세 조회 실패: 권한 없음")
        void findOneTemplateFailWithUnauthorized() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSnippets("title");
            templateService.createTemplate(templateRequest, memberDto);

            // when
            String basicAuth = HttpHeaders.encodeBasicAuth(secondMember.getEmail(), secondMember.getPassword(), StandardCharsets.UTF_8);
            cookie = new Cookie("Authorization", basicAuth);

            // then
            mvc.perform(get("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("해당 템플릿에 대한 권한이 없습니다."));
        }
    }

    @Nested
    @DisplayName("템플릿 수정 테스트")
    class updateTemplateTest {

        @Test
        @DisplayName("템플릿 수정 성공")
        void updateTemplateSuccess() throws Exception {
            // given
            createTemplateAndTwoCategories();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "updateTitle",
                    "description",
                    List.of(
                            new CreateSnippetRequest("filename3", "content3", 2),
                            new CreateSnippetRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSnippetRequest(2L, "updateFilename2", "updateContent2", 1)
                    ),
                    List.of(1L),
                    1L,
                    List.of("tag1", "tag3")
            );

            // when & then
            mvc.perform(post("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTemplateRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("템플릿 수정 실패: 권한 없음")
        void updateTemplateFailWithUnauthorized() throws Exception {
            // given
            createTemplateAndTwoCategories();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "updateTitle",
                    "description",
                    List.of(
                            new CreateSnippetRequest("filename3", "content3", 2),
                            new CreateSnippetRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSnippetRequest(2L, "updateFilename2", "updateContent2", 1)
                    ),
                    List.of(1L),
                    2L,
                    List.of("tag1", "tag3")
            );

            // when
            String basicAuth = HttpHeaders.encodeBasicAuth(secondMember.getEmail(), secondMember.getPassword(), StandardCharsets.UTF_8);
            cookie = new Cookie("Authorization", basicAuth);

            // then
            mvc.perform(post("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTemplateRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("해당 템플릿에 대한 권한이 없습니다."));
        }

        @Test
        @DisplayName("템플릿 수정 실패: 템플릿 이름 길이 초과")
        void updateTemplateFailWithLongName() throws Exception {
            // given
            String exceededTitle = "a".repeat(MAX_LENGTH + 1);
            createTemplateAndTwoCategories();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    exceededTitle,
                    "description",
                    List.of(
                            new CreateSnippetRequest("filename3", "content3", 2),
                            new CreateSnippetRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSnippetRequest(2L, "updateFilename2", "updateContent2", 1)
                    ),
                    List.of(1L),
                    2L,
                    List.of("tag1", "tag3")
            );

            // when & then
            mvc.perform(post("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTemplateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("템플릿명은 최대 255자까지 입력 가능합니다."));
        }

        @Test
        @DisplayName("템플릿 수정 실패: 파일 이름 길이 초과")
        void updateTemplateFailWithLongFileName() throws Exception {
            // given
            String exceededTitle = "a".repeat(MAX_LENGTH + 1);
            createTemplateAndTwoCategories();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "title",
                    "description",
                    List.of(
                            new CreateSnippetRequest(exceededTitle, "content3", 2),
                            new CreateSnippetRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSnippetRequest(2L, "updateFilename2", "updateContent2", 1)
                    ),
                    List.of(1L),
                    2L,
                    List.of("tag1", "tag3")
            );

            // when & then
            mvc.perform(post("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTemplateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("파일명은 최대 255자까지 입력 가능합니다."));
        }

        @ParameterizedTest
        @DisplayName("템플릿 수정 실패: 파일 내용 길이 초과")
        @CsvSource({"a, 65536", "ㄱ, 21846"})
        void updateTemplateFailWithLongFileContent(String repeatTarget, int exceedLength) throws Exception {
            // given
            createTemplateAndTwoCategories();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "title",
                    "description",
                    List.of(
                            new CreateSnippetRequest("filename3", repeatTarget.repeat(exceedLength), 2),
                            new CreateSnippetRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSnippetRequest(2L, "updateFilename2", "updateContent2", 1)
                    ),
                    List.of(1L),
                    2L,
                    List.of("tag1", "tag3")
            );

            // when & then
            mvc.perform(post("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTemplateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("소스 코드는 최대 65,535 Byte까지 입력 가능합니다."));
        }

        @ParameterizedTest
        @DisplayName("템플릿 수정 실패: 템플릿 설명 길이 초과")
        @CsvSource({"a, 65536", "ㄱ, 21846"})
        void updateTemplateFailWithLongContent(String repeatTarget, int exceedLength) throws Exception {
            // given
            createTemplateAndTwoCategories();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "title",
                    repeatTarget.repeat(exceedLength),
                    List.of(
                            new CreateSnippetRequest("filename3", "content3", 2),
                            new CreateSnippetRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSnippetRequest(2L, "updateFilename2", "updateContent2", 1)
                    ),
                    List.of(1L),
                    2L,
                    List.of("tag1", "tag3")
            );

            // when & then
            mvc.perform(post("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTemplateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("템플릿 설명은 최대 65,535 Byte까지 입력 가능합니다."));
        }

        // 정상 요청: 2, 3, 1
        @ParameterizedTest
        @DisplayName("템플릿 수정 실패: 잘못된 스니펫 순서 입력")
        @CsvSource({"1, 2, 1", "3, 2, 1", "0, 2, 1"})
        void updateTemplateFailWithWrongSnippetOrdinal(int createOrdinal1, int createOrdinal2, int updateOrdinal)
                throws Exception {
            // given
            createTemplateAndTwoCategories();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "updateTitle",
                    "description",
                    List.of(
                            new CreateSnippetRequest("filename3", "content3", createOrdinal1),
                            new CreateSnippetRequest("filename4", "content4", createOrdinal2)
                    ),
                    List.of(
                            new UpdateSnippetRequest(2L, "updateFilename2", "updateContent2", updateOrdinal)
                    ),
                    List.of(1L),
                    2L,
                    List.of("tag1", "tag3")
            );

            // when & then
            mvc.perform(post("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTemplateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("스니펫 순서가 잘못되었습니다."));
        }

        private void createTemplateAndTwoCategories() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(new CreateCategoryRequest("category1"), memberDto);
            categoryService.create(new CreateCategoryRequest("category2"), memberDto);
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSnippets("title");
            templateService.createTemplate(templateRequest, memberDto);
        }
    }

    @Nested
    @DisplayName("템플릿 삭제 테스트")
    class deleteTemplateTest {

        @Test
        @DisplayName("템플릿 삭제 성공")
        void deleteTemplateSuccess() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(new CreateCategoryRequest("category"), memberDto);
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSnippets("title");
            templateService.createTemplate(templateRequest, memberDto);

            // when & then
            mvc.perform(delete("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 권한 없음")
        void deleteTemplateFailWithUnauthorized() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(new CreateCategoryRequest("category"), memberDto);
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSnippets("title");
            templateService.createTemplate(templateRequest, memberDto);

            // when
            String basicAuth = HttpHeaders.encodeBasicAuth(secondMember.getEmail(), secondMember.getPassword(), StandardCharsets.UTF_8);
            cookie = new Cookie("Authorization", basicAuth);

            // then
            mvc.perform(delete("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("해당 템플릿에 대한 권한이 없습니다."));
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 존재하지 않는 템플릿 삭제")
        void deleteTemplateFailWithNotFoundTemplate() throws Exception {
            // when & then
            mvc.perform(delete("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    private static CreateTemplateRequest createTemplateRequestWithTwoSnippets(String title) {
        CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                title,
                "description",
                List.of(
                        new CreateSnippetRequest("filename1", "content1", 1),
                        new CreateSnippetRequest("filename2", "content2", 2)
                ),
                1,
                1L,
                List.of("tag1", "tag2")
        );
        return templateRequest;
    }
}
