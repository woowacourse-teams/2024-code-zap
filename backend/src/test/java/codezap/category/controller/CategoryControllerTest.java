package codezap.category.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateAllCategoriesRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.fixture.MemberFixture;
import codezap.global.MockMvcTest;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;

@Import(CategoryController.class)
class CategoryControllerTest extends MockMvcTest {

    private static final int MAX_LENGTH = 255;

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class CreateCategoryTest {

        @Test
        @DisplayName("카테고리 생성 성공")
        void createCategorySuccess() throws Exception {
            // given
            long categoryId = 1L;
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category", 1);

            when(categoryService.create(
                    MemberFixture.getFirstMember(), createCategoryRequest))
                    .thenReturn(new CreateCategoryResponse(1L, "category", 1));

            // when & then
            mvc.perform(post("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createCategoryRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(redirectedUrl("/categories/" + categoryId));
        }

        @Test
        @DisplayName("카테고리 생성 실패: 로그인을 하지 않은 회원")
        void createCategoryFailWithNotLogin() throws Exception {
            // given
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category", 1);

            doThrow(new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."))
                    .when(credentialManager).getCredential(any());

            // when & then
            mvc.perform(post("/categories")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createCategoryRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."))
                    .andExpect(jsonPath("$.errorCode").value(1301));
        }

        @Test
        @DisplayName("카테고리 생성 실패: 카테고리 이름 길이 초과")
        void createCategoryFailWithLongName() throws Exception {
            // given
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("a".repeat(MAX_LENGTH + 1), 1);

            // when & then
            mvc.perform(post("/categories")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createCategoryRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("카테고리 이름은 최대 15자까지 입력 가능합니다."))
                    .andExpect(jsonPath("$.errorCode").value(1101));
        }
    }

    @Test
    @DisplayName("카테고리 전체 조회 성공")
    void findAllCategoriesSuccess() throws Exception {
        // given
        Member member = MemberFixture.getFirstMember();
        List<Category> categories = List.of(new Category("category1", member, 1), new Category("category1", member, 2));
        FindAllCategoriesResponse findAllCategoriesResponse = FindAllCategoriesResponse.from(categories);

        when(categoryService.findAllByMemberId(any())).thenReturn(findAllCategoriesResponse);

        // when & then
        mvc.perform(get("/categories")
                        .param("memberId", "1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories").isArray())
                .andExpect(jsonPath("$.categories[0].name").value(categories.get(0).getName()))
                .andExpect(jsonPath("$.categories[1].name").value(categories.get(1).getName()));
    }

    @Nested
    @DisplayName("카테고리 편집 테스트")
    class UpdateCategoryTest {

        @Test
        @DisplayName("카테고리 편집 성공")
        void updateCategorySuccess() throws Exception {
            // given
            var updateCategoryRequest = new UpdateCategoryRequest(1L, "updateCategory", 1);
            var request = new UpdateAllCategoriesRequest(
                    List.of(),
                    List.of(updateCategoryRequest),
                    List.of());

            // when & then
            mvc.perform(put("/categories")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("카테고리 편집 실패: 로그인 되지 않은 경우")
        void updateCategoryFailWithUnauthorized() throws Exception {
            // given
            var updateCategoryRequest = new UpdateCategoryRequest(1L, "a".repeat(MAX_LENGTH), 1);
            var request = new UpdateAllCategoriesRequest(
                    List.of(),
                    List.of(updateCategoryRequest),
                    List.of());

            doThrow(new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."))
                    .when(credentialManager).getCredential(any());

            // when & then
            mvc.perform(put("/categories")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("카테고리 편집 실패: 카테고리 이름 길이 초과")
        void updateCategoryFailWithLongName() throws Exception {
            // given
            var updateCategoryRequest = new UpdateCategoryRequest(1L, "a".repeat(MAX_LENGTH + 1), 1);
            var request = new UpdateAllCategoriesRequest(
                    List.of(),
                    List.of(updateCategoryRequest),
                    List.of());

            // when & then
            mvc.perform(put("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("카테고리 이름은 최대 15자까지 입력 가능합니다."))
                    .andExpect(jsonPath("$.errorCode").value(1101));
        }

        @Test
        @DisplayName("카테고리 편집 실패: 중복된 순서")
        void duplicatedCategoryOrdinal() throws Exception {
            CreateCategoryRequest createRequest = new CreateCategoryRequest("category3", 1);
            UpdateCategoryRequest updateRequest = new UpdateCategoryRequest(1L, "category1", 1);

            var request = new UpdateAllCategoriesRequest(
                    List.of(createRequest),
                    List.of(updateRequest),
                    List.of());

            mvc.perform(put("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("순서가 잘못되었습니다."))
                    .andExpect(jsonPath("$.errorCode").value(1101));
        }

        @Test
        @DisplayName("카테고리 편집 실패: 연속되지 않는 순서")
        void nonSequentialCategoryOrdinal() throws Exception {
            CreateCategoryRequest createRequest = new CreateCategoryRequest("category3", 3);
            UpdateCategoryRequest updateRequest = new UpdateCategoryRequest(1L, "category1", 1);

            var request = new UpdateAllCategoriesRequest(
                    List.of(createRequest),
                    List.of(updateRequest),
                    List.of());

            mvc.perform(put("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("순서가 잘못되었습니다."))
                    .andExpect(jsonPath("$.errorCode").value(1101));
        }

        @Test
        @DisplayName("카테고리 편집 실패: 중복된 카테고리 이름")
        void duplicatedCategoryName() throws Exception {
            String duplicatedName = "duplicatedName";
            CreateCategoryRequest createRequest = new CreateCategoryRequest(duplicatedName, 2);
            UpdateCategoryRequest updateRequest = new UpdateCategoryRequest(1L, duplicatedName, 1);

            var request = new UpdateAllCategoriesRequest(
                    List.of(createRequest),
                    List.of(updateRequest),
                    List.of());

            mvc.perform(put("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("카테고리명이 중복되었습니다."))
                    .andExpect(jsonPath("$.errorCode").value(1101));
        }

        @Test
        @DisplayName("카테고리 편집 실패: 중복된 id 수정 및 삭제")
        void deleteByIdFailDuplicatedId() throws Exception {
            UpdateCategoryRequest updateRequest = new UpdateCategoryRequest(1L, "category1", 1);

            var request = new UpdateAllCategoriesRequest(
                    List.of(),
                    List.of(updateRequest),
                    List.of(1L));

            mvc.perform(put("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("id가 중복되었습니다."))
                    .andExpect(jsonPath("$.errorCode").value(1101));
        }
    }
}
