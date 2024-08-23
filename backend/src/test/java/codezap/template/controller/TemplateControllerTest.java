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

import codezap.auth.configuration.AuthArgumentResolver;
import codezap.auth.manager.CookieCredentialManager;
import codezap.auth.provider.basic.BasicAuthCredentialProvider;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.category.service.CategoryService;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberDtoFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.GlobalExceptionHandler;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.member.service.MemberService;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.FakeSourceCodeRepository;
import codezap.template.repository.FakeTagRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.FakeTemplateTagRepository;
import codezap.template.repository.FakeThumbnailRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.service.TemplateApplicationService;
import codezap.template.service.TemplateService;

class TemplateControllerTest {

    private static final int MAX_LENGTH = 255;

    private final TemplateRepository templateRepository = new FakeTemplateRepository();
    private final CategoryRepository categoryRepository = new FakeCategoryRepository(
            List.of(CategoryFixture.getFirstCategory(), CategoryFixture.getSecondCategory())
    );
    private final MemberRepository memberRepository = new FakeMemberRepository(
            List.of(MemberFixture.getFirstMember(), MemberFixture.getSecondMember())
    );
    private final TemplateService templateService = new TemplateService(
            new FakeThumbnailRepository(),
            templateRepository,
            new FakeSourceCodeRepository(),
            categoryRepository,
            new FakeTagRepository(),
            new FakeTemplateTagRepository(),
            memberRepository);
    private final CategoryService categoryService = new CategoryService(
            categoryRepository, templateRepository, memberRepository
    );

    private final TemplateApplicationService applicationService = new TemplateApplicationService(
            new MemberService(memberRepository, categoryRepository)
            , templateService
    );

    private final MockMvc mvc =
            MockMvcBuilders.standaloneSetup(new TemplateController(templateService, applicationService))
                    .setControllerAdvice(new GlobalExceptionHandler())
                    .setCustomArgumentResolvers(
                            new AuthArgumentResolver(
                                    new CookieCredentialManager(),
                                    new BasicAuthCredentialProvider(memberRepository)
                            ),
                            new PageableHandlerMethodArgumentResolver())
                    .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    Cookie cookie;

    @BeforeEach
    void setCookie() {
        Member firstMember = MemberFixture.getFirstMember();
        String basicAuth = HttpHeaders.encodeBasicAuth(
                firstMember.getName(),
                firstMember.getPassword(),
                StandardCharsets.UTF_8);
        cookie = new Cookie("credential", basicAuth);
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
                    List.of(new CreateSourceCodeRequest("a".repeat(MAX_LENGTH), repeatTarget.repeat(maxLength), 1)),
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
        @DisplayName("템플릿 생성 실패: 템플릿명 길이 초과")
        void createTemplateFailWithLongTitle() throws Exception {
            String exceededTitle = "a".repeat(MAX_LENGTH + 1);
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    exceededTitle,
                    "description",
                    List.of(new CreateSourceCodeRequest("a", "content", 1)),
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

        @ParameterizedTest
        @DisplayName("템플릿 생성 실패: 템플릿 설명 길이 초과")
        @CsvSource({"a, 65536", "ㄱ, 21846"})
        void createTemplateFailWithLongDescription(String repeatTarget, int exceededLength) throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    repeatTarget.repeat(exceededLength),
                    List.of(new CreateSourceCodeRequest("title", "content", 1)),
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

        @Test
        @DisplayName("템플릿 생성 실패: 파일명 길이 초과")
        void createTemplateFailWithLongFileName() throws Exception {
            String exceededTitle = "a".repeat(MAX_LENGTH + 1);
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest(exceededTitle, "content", 1)),
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
        @DisplayName("템플릿 생성 실패: 소스 코드 길이 초과")
        @CsvSource({"a, 65536", "ㄱ, 21846"})
        void createTemplateFailWithLongContent(String repeatTarget, int exceededLength) throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest("title", repeatTarget.repeat(exceededLength), 1)),
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
        @DisplayName("템플릿 생성 실패: 잘못된 소스 코드 순서 입력")
        @CsvSource({"0, 1", "1, 3", "2, 1"})
        void createTemplateFailWithWrongSourceCodeOrdinal(int firstIndex, int secondIndex) throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest("title", "content", firstIndex),
                            new CreateSourceCodeRequest("title", "content", secondIndex)),
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
                    .andExpect(jsonPath("$.detail").value("소스 코드 순서가 잘못되었습니다."));
        }

        @Test
        @DisplayName("템플릿 생성 실패: 인증 정보가 없거나 잘못된 경우")
        void createTemplateFailWithNotLogin() throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest("filename", "content", 1)),
                    1,
                    1L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."));
        }

        @Test
        @DisplayName("템플릿 생성 실패: 카테고리 권한이 없는 경우")
        void createTemplateFailWithNoMatchCategory() throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest("filename", "content", 1)),
                    1,
                    2L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("해당 카테고리에 대한 권한이 없습니다."));
        }

        @Test
        @DisplayName("템플릿 생성 실패: 카테고리가 없는 경우")
        void createTemplateFailWithNotCategory() throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest("filename", "content", 1)),
                    1,
                    3L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("식별자 3에 해당하는 카테고리가 존재하지 않습니다."));
        }

        @Test
        @DisplayName("템플릿 생성 실패: 해당 순서인 소스 코드 없는 경우")
        void createTemplateFailWithNotSourceCode() throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest("filename", "content", 1)),
                    10,
                    1L,
                    List.of("tag1", "tag2")
            );

            mvc.perform(post("/templates")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("템플릿에 10번째 소스 코드가 존재하지 않습니다."));
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
            CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSourceCodes("title1");
            CreateTemplateRequest templateRequest2 = createTemplateRequestWithTwoSourceCodes("title2");
            templateService.createTemplate(memberDto, templateRequest1);
            templateService.createTemplate(memberDto, templateRequest2);

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
            CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSourceCodes("title1");
            templateService.createTemplate(memberDto, templateRequest1);

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

        @Test
        @DisplayName("템플릿 검색 실패: 인증 정보가 없거나 잘못된 경우")
        void findAllTemplatesFailWithCookie() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSourceCodes("title1");
            templateService.createTemplate(memberDto, templateRequest1);

            // when & then
            mvc.perform(get("/templates")
                            .param("memberId", "1")
                            .param("keyword", "")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."));
        }

        @Test
        @DisplayName("템플릿 검색 실패: 인증 정보와 멤버 ID가 다른 경우")
        void findAllTemplatesFailNonMatchMemberIdAndMemberDto() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSourceCodes("title1");
            templateService.createTemplate(memberDto, templateRequest1);

            // when & then
            mvc.perform(get("/templates")
                            .cookie(cookie)
                            .param("memberId", "100")
                            .param("keyword", "")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("인증 정보에 포함된 멤버 ID와 파라미터로 받은 멤버 ID가 다릅니다."));
        }

        @Test
        @DisplayName("템플릿 검색 실패: 카테고리가 없는 경우")
        void findAllTemplatesFailNotFoundCategory() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSourceCodes("title1");
            templateService.createTemplate(memberDto, templateRequest1);

            // when & then
            mvc.perform(get("/templates")
                            .cookie(cookie)
                            .param("memberId", "1")
                            .param("keyword", "")
                            .param("categoryId", "100")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("식별자 100에 해당하는 카테고리가 존재하지 않습니다."));
        }

        @Test
        @DisplayName("템플릿 검색 실패: 태그가 없는 경우")
        void findAllTemplatesFailNotFoundTag() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSourceCodes("title1");
            templateService.createTemplate(memberDto, templateRequest1);

            // when & then
            mvc.perform(get("/templates")
                            .cookie(cookie)
                            .param("memberId", "1")
                            .param("keyword", "")
                            .param("tagIds", "100")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("식별자 100에 해당하는 태그가 존재하지 않습니다."));
        }
    }

    @Nested
    @DisplayName("템플릿 단건 조회 테스트")
    class findTemplateTest {
        @Test
        @DisplayName("템플릿 단건 조회 성공")
        void findOneTemplateSuccess() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSourceCodes("title");
            templateService.createTemplate(memberDto, templateRequest);

            // when & then
            mvc.perform(get("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(templateRequest.title()))
                    .andExpect(jsonPath("$.sourceCodes.size()").value(2))
                    .andExpect(jsonPath("$.category.id").value(1))
                    .andExpect(jsonPath("$.category.name").value("카테고리 없음"))
                    .andExpect(jsonPath("$.tags.size()").value(2));
        }

        @Test
        @DisplayName("템플릿 단건 조회 실패: 존재하지 않는 템플릿 조회")
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
        @DisplayName("템플릿 단건 조회 실패: 자신의 템플릿이 아닐 경우")
        void findOneTemplateFailWithUnauthorized() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSourceCodes("title");
            templateService.createTemplate(memberDto, templateRequest);

            // then
            mvc.perform(get("/templates/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."));
        }

        @Test
        @DisplayName("템플릿 단건 조회 실패: 자신의 템플릿이 아닐 경우")
        void findOneTemplateFailWithNotMine() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSourceCodes("title");
            templateService.createTemplate(memberDto, templateRequest);
            Member secondMember = MemberFixture.getSecondMember();

            // when
            String basicAuth = HttpHeaders.encodeBasicAuth(
                    secondMember.getName(),
                    secondMember.getPassword(),
                    StandardCharsets.UTF_8);
            cookie = new Cookie("credential", basicAuth);

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
                            new CreateSourceCodeRequest("filename3", "content3", 2),
                            new CreateSourceCodeRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", 1)
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
                            new CreateSourceCodeRequest("filename3", "content3", 2),
                            new CreateSourceCodeRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", 1)
                    ),
                    List.of(1L),
                    2L,
                    List.of("tag1", "tag3")
            );
            Member secondMember = MemberFixture.getSecondMember();

            // when
            String basicAuth = HttpHeaders.encodeBasicAuth(
                    secondMember.getName(),
                    secondMember.getPassword(),
                    StandardCharsets.UTF_8);
            cookie = new Cookie("credential", basicAuth);

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
        @DisplayName("템플릿 수정 실패: 템플릿명 길이 초과")
        void updateTemplateFailWithLongName() throws Exception {
            // given
            String exceededTitle = "a".repeat(MAX_LENGTH + 1);
            createTemplateAndTwoCategories();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    exceededTitle,
                    "description",
                    List.of(
                            new CreateSourceCodeRequest("filename3", "content3", 2),
                            new CreateSourceCodeRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", 1)
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
                            new CreateSourceCodeRequest(exceededTitle, "content3", 2),
                            new CreateSourceCodeRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", 1)
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
                            new CreateSourceCodeRequest("filename3", repeatTarget.repeat(exceedLength), 2),
                            new CreateSourceCodeRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", 1)
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
                            new CreateSourceCodeRequest("filename3", "content3", 2),
                            new CreateSourceCodeRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", 1)
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
        @DisplayName("템플릿 수정 실패: 잘못된 소스 코드 순서 입력")
        @CsvSource({"1, 2, 1", "3, 2, 1", "0, 2, 1"})
        void updateTemplateFailWithWrongSourceCodeOrdinal(int createOrdinal1, int createOrdinal2, int updateOrdinal)
                throws Exception {
            // given
            createTemplateAndTwoCategories();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "updateTitle",
                    "description",
                    List.of(
                            new CreateSourceCodeRequest("filename3", "content3", createOrdinal1),
                            new CreateSourceCodeRequest("filename4", "content4", createOrdinal2)
                    ),
                    List.of(
                            new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", updateOrdinal)
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
                    .andExpect(jsonPath("$.detail").value("소스 코드 순서가 잘못되었습니다."));
        }

        private void createTemplateAndTwoCategories() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(memberDto, new CreateCategoryRequest("category1"));
            categoryService.create(memberDto, new CreateCategoryRequest("category2"));
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSourceCodes("title");
            templateService.createTemplate(memberDto, templateRequest);
        }
    }

    @Nested
    @DisplayName("템플릿 삭제 테스트")
    class deleteTemplateTest {

        @Test
        @DisplayName("템플릿 삭제 성공: 1개")
        void deleteTemplateSuccess() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(memberDto, new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSourceCodes("title");
            templateService.createTemplate(memberDto, templateRequest);

            // when & then
            mvc.perform(delete("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("템플릿 삭제 성공: 여러개")
        void deleteTemplatesSuccess() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(memberDto, new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSourceCodes("title");
            CreateTemplateRequest templateRequest2 = createTemplateRequestWithTwoSourceCodes("title");
            templateService.createTemplate(memberDto, templateRequest1);
            templateService.createTemplate(memberDto, templateRequest2);

            // when & then
            mvc.perform(delete("/templates/1,2")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 템플릿 ID가 중복된 경우")
        void deleteTemplateFailWithDuplication() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(memberDto, new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSourceCodes("title");
            templateService.createTemplate(memberDto, templateRequest);

            // when & then
            mvc.perform(delete("/templates/1,1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("삭제하고자 하는 템플릿 ID가 중복되었습니다."));
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 인증 정보가 없거나 잘못된 경우")
        void deleteTemplateFailWithUnauthorized() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(memberDto, new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSourceCodes("title");
            templateService.createTemplate(memberDto, templateRequest);

            // when & then
            mvc.perform(delete("/templates/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."));
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 자신의 템플릿이 아닐 경우")
        void deleteTemplateFailNotMine() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(memberDto, new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSourceCodes("title");
            templateService.createTemplate(memberDto, templateRequest);
            Member secondMember = MemberFixture.getSecondMember();

            // when
            String basicAuth = HttpHeaders.encodeBasicAuth(
                    secondMember.getName(),
                    secondMember.getPassword(),
                    StandardCharsets.UTF_8);
            cookie = new Cookie("credential", basicAuth);

            // then
            mvc.perform(delete("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("해당 템플릿에 대한 권한이 없습니다."));
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 존재 하지 않는 템플릿 삭제")
        void deleteTemplateFailWithNotFoundTemplate() throws Exception {
            // when & then
            mvc.perform(delete("/templates/1")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("식별자 1에 해당하는 템플릿이 존재하지 않습니다."));
        }
    }

    private static CreateTemplateRequest createTemplateRequestWithTwoSourceCodes(String title) {
        return new CreateTemplateRequest(
                title,
                "description",
                List.of(
                        new CreateSourceCodeRequest("filename1", "content1", 1),
                        new CreateSourceCodeRequest("filename2", "content2", 2)
                ),
                1,
                1L,
                List.of("tag1", "tag2")
        );
    }
}
