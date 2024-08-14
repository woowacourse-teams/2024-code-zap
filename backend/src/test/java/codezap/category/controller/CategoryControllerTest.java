package codezap.category.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
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
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
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
import codezap.template.service.TemplateService;

class CategoryControllerTest {

    private static final int MAX_LENGTH = 255;

    private Member firstMember = new Member(1L, "test1@email.com", "password1234", "username1");
    private Member secondMember = new Member(2L, "test2@email.com", "password1234", "username2");
    private Category firstCategory = new Category(1L, firstMember, "카테고리 없음", true);

    private final TemplateRepository templateRepository = new FakeTemplateRepository();
    private final SnippetRepository snippetRepository = new FakeSnippetRepository();
    private final ThumbnailSnippetRepository thumbnailSnippetRepository = new FakeThumbnailSnippetRepository();
    private final CategoryRepository categoryRepository = new FakeCategoryRepository(List.of(firstCategory));
    private final TemplateTagRepository templateTagRepository = new FakeTemplateTagRepository();
    private final TagRepository tagRepository = new FakeTagRepository();
    private final MemberRepository memberRepository = new FakeMemberRepository(List.of(firstMember));
    private final TemplateService templateService = new TemplateService(
            thumbnailSnippetRepository,
            templateRepository,
            snippetRepository,
            categoryRepository,
            tagRepository,
            templateTagRepository,
            memberRepository);
    private CategoryService categoryService = new CategoryService(categoryRepository, templateRepository,
            memberRepository);
    private final AuthService authService = new AuthService(memberRepository);
    private final CategoryController categoryController = new CategoryController(categoryService);

    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(categoryController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new AuthArgumentResolver(authService),
                    new PageableHandlerMethodArgumentResolver())
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    Cookie cookie;

    @BeforeEach
    void setting() {
        String basicAuth = HttpHeaders.encodeBasicAuth(firstMember.getEmail(), firstMember.getPassword(), StandardCharsets.UTF_8);
        cookie = new Cookie("Authorization", basicAuth);
    }

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class createCategoryTest {
        @Test
        @DisplayName("카테고리 생성 성공")
        void createCategorySuccess() throws Exception {
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("a".repeat(MAX_LENGTH));

            mvc.perform(post("/categories")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createCategoryRequest)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("카테고리 생성 실패: 로그인을 하지 않은 유저")
        void createCategoryFailWithNotLogin() throws Exception {
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("a".repeat(MAX_LENGTH));

            mvc.perform(post("/categories")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createCategoryRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("카테고리 생성 실패: 카테고리 이름 길이 초과")
        void createCategoryFailWithLongName() throws Exception {
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("a".repeat(MAX_LENGTH + 1));

            mvc.perform(post("/categories")
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createCategoryRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("카테고리 이름은 최대 255자까지 입력 가능합니다."));
        }
    }

    @Test
    @DisplayName("카테고리 전체 조회 성공")
    void findAllCategoriesSuccess() throws Exception {
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category1");
        categoryService.create(createCategoryRequest, memberDto);

        mvc.perform(get("/categories")
                        .cookie(cookie)
                        .param("memberId", "1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories.size()").value(2));
    }

    @Nested
    @DisplayName("카테고리 수정 테스트")
    class updateCategoryTest {

        Long savedCategoryId;

        @BeforeEach
        void saveCategory() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            savedCategoryId = categoryService.create(new CreateCategoryRequest("category1"), memberDto);
        }

        @Test
        @DisplayName("카테고리 수정 성공")
        void updateCategorySuccess() throws Exception {
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("a".repeat(MAX_LENGTH));

            mvc.perform(put("/categories/" + savedCategoryId)
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("카테고리 수정 실패: 권한 없음")
        void updateCategoryFailWithUnauthorized() throws Exception {
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("a".repeat(MAX_LENGTH));

            // when
            String basicAuth = HttpHeaders.encodeBasicAuth(secondMember.getEmail(), secondMember.getPassword(), StandardCharsets.UTF_8);
            cookie = new Cookie("Authorization", basicAuth);

            mvc.perform(put("/categories/" + savedCategoryId)
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("카테고리 수정 실패: 카테고리 이름 길이 초과")
        void updateCategoryFailWithLongName() throws Exception {
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("a".repeat(MAX_LENGTH + 1));

            mvc.perform(put("/categories/" + savedCategoryId)
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("카테고리 이름은 최대 255자까지 입력 가능합니다."));
        }

        @Test
        @DisplayName("카테고리 수정 실패: 중복된 이름의 카테고리 존재")
        void updateCategoryFailWithDuplicatedName() throws Exception {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            String duplicatedName = "duplicatedName";
            categoryService.create(new CreateCategoryRequest(duplicatedName), memberDto);

            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(duplicatedName);

            // when & then
            mvc.perform(put("/categories/" + savedCategoryId)
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.detail").value("이름이 " + duplicatedName + "인 카테고리가 이미 존재합니다."));
        }
    }


    @Nested
    @DisplayName("카테고리 삭제 테스트")
    class deleteCategoryTest {

        Long savedCategoryId;

        @BeforeEach
        void saveCategory() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            categoryService.create(new CreateCategoryRequest("category1"), memberDto);
            savedCategoryId = categoryService.create(new CreateCategoryRequest("category2"), memberDto);
        }

        @Test
        @DisplayName("카테고리 삭제 성공")
        void deleteCategorySuccess() throws Exception {
            mvc.perform(delete("/categories/" + savedCategoryId)
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("카테고리 삭제 실패: 권한 없음")
        void deleteCategoryFailWithUnauthorized() throws Exception {
            String basicAuth = HttpHeaders.encodeBasicAuth(secondMember.getEmail(), secondMember.getPassword(), StandardCharsets.UTF_8);
            cookie = new Cookie("Authorization", basicAuth);

            mvc.perform(delete("/categories/" + savedCategoryId)
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("카테고리 삭제 실패: 존재하지 않는 카테고리의 삭제 요청")
        void updateCategoryFailWithDuplicatedName() throws Exception {
            long id = 12;
            mvc.perform(delete("/categories/" + id)
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("식별자 " + id + "에 해당하는 카테고리가 존재하지 않습니다."));
        }

        @Test
        @DisplayName("카테고리 삭제 실패: 템플릿이 존재하는 카테고리는 삭제 불가능")
        void updateCategoryFailWithLongName() throws Exception {
            // given
            templateService.createTemplate(
                    MemberDtoFixture.getFirstMemberDto(),
                    new CreateTemplateRequest(
                            "title",
                            "description",
                            List.of(new CreateSnippetRequest("filename", "content", 1)),
                            1,
                            savedCategoryId,
                            List.of("tag1", "tag2")
                    )
            );

            // when & then
            mvc.perform(delete("/categories/" + savedCategoryId)
                            .cookie(cookie)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("템플릿이 존재하는 카테고리는 삭제할 수 없습니다."));
        }
    }
}
