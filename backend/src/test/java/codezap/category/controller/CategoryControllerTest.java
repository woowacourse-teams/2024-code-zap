package codezap.category.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.global.MockMvcTest;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;

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
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category");

            when(categoryService.create(
                    MemberFixture.memberFixture(), createCategoryRequest))
                    .thenReturn(new CreateCategoryResponse(1L, "category"));

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
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category");

            doThrow(new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."))
                    .when(credentialProvider).extractMember(anyString());

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
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("a".repeat(MAX_LENGTH + 1));

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
        Member member = MemberFixture.memberFixture();
        List<Category> categories = List.of(new Category("category1", member), new Category("category1", member));
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
    @DisplayName("카테고리 수정 테스트")
    class UpdateCategoryTest {

        @Test
        @DisplayName("카테고리 수정 성공")
        void updateCategorySuccess() throws Exception {
            // given
            long categoryId = 1L;
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("updateCategory");

            // when & then
            mvc.perform(put("/categories/" + categoryId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("카테고리 수정 실패: 권한 없음")
        void updateCategoryFailWithUnauthorized() throws Exception {
            // given
            long categoryId = 1L;
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("a".repeat(MAX_LENGTH));

            doThrow(new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."))
                    .when(credentialProvider).extractMember(anyString());

            // when & then
            mvc.perform(put("/categories/" + categoryId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("카테고리 수정 실패: 카테고리 이름 길이 초과")
        void updateCategoryFailWithlongName() throws Exception {
            // given
            long categoryId = 1L;
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("a".repeat(MAX_LENGTH + 1));

            // when & then
            mvc.perform(put("/categories/" + categoryId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("카테고리 이름은 최대 255자까지 입력 가능합니다."))
                    .andExpect(jsonPath("$.errorCode").value(1101));
        }

        @Test
        @DisplayName("카테고리 수정 실패: 중복된 이름의 카테고리 존재")
        void updateCategoryFailWithDuplicatedName() throws Exception {
            // given
            long categoryId = 1L;
            String duplicatedName = "duplicatedName";
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(duplicatedName);

            doThrow(new CodeZapException(ErrorCode.DUPLICATE_CATEGORY, "이름이 " + duplicatedName + "인 카테고리가 이미 존재합니다."))
                    .when(categoryService).update(any(), any(), any());

            // when & then
            mvc.perform(put("/categories/" + categoryId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.detail").value("이름이 " + duplicatedName + "인 카테고리가 이미 존재합니다."))
                    .andExpect(jsonPath("$.errorCode").value(1203));
        }
    }

    @Nested
    @DisplayName("카테고리 삭제 테스트")
    class DeleteCategoryTest {

        @Test
        @DisplayName("카테고리 삭제 성공")
        void deleteCategorySuccess() throws Exception {
            // given
            long categoryId = 1L;

            // when
            mvc.perform(delete("/categories/" + categoryId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            // then
            verify(categoryService, times(1))
                    .deleteById(MemberFixture.memberFixture(), categoryId);
        }

        @Test
        @DisplayName("카테고리 삭제 실패: 권한 없음")
        void deleteCategoryFailWithUnauthorized() throws Exception {
            // given
            long categoryId = 1L;
            doThrow(new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."))
                    .when(credentialProvider).extractMember(anyString());

            // when & then
            mvc.perform(delete("/categories/" + categoryId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."))
                    .andExpect(jsonPath("$.errorCode").value(1301));
        }

        @Test
        @DisplayName("카테고리 삭제 실패: 존재하지 않는 카테고리의 삭제 요청")
        void deleteCategoryFailWithDuplicatedName() throws Exception {
            long id = 2L;

            doThrow(new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 카테고리가 존재하지 않습니다."))
                    .when(categoryService).deleteById(any(), any());

            mvc.perform(delete("/categories/" + id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("식별자 " + id + "에 해당하는 카테고리가 존재하지 않습니다."))
                    .andExpect(jsonPath("$.errorCode").value(1201));
        }

        @Test
        @DisplayName("카테고리 삭제 실패: 템플릿이 존재하는 카테고리는 삭제 불가능")
        void deleteCategoryFailWithlongName() throws Exception {
            long categoryId = 1L;
            doThrow(new CodeZapException(ErrorCode.CATEGORY_HAS_TEMPLATES, "템플릿이 존재하는 카테고리는 삭제할 수 없습니다."))
                    .when(categoryService).deleteById(any(), any());

            mvc.perform(delete("/categories/" + categoryId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("템플릿이 존재하는 카테고리는 삭제할 수 없습니다."))
                    .andExpect(jsonPath("$.errorCode").value(1102));
        }
    }
}
