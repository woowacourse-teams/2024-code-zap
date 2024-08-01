package codezap.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
class CategoryServiceTest {

    @LocalServerPort
    int port;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setting() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class createCategoryTest {
        @Test
        @DisplayName("카테고리 생성 성공")
        void createCategorySuccess() {
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category1");

            Long categoryId = categoryService.create(createCategoryRequest);

            assertThat(categoryId).isEqualTo(1L);
        }

        @Test
        @DisplayName("카테고리 생성 실패: 중복된 이름의 카테고리 이름 생성")
        void createCategoryFailWithDuplicateName() {
            categoryRepository.save(new Category("category"));
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category");

            assertThatThrownBy(() -> categoryService.create(createCategoryRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + createCategoryRequest.name() + "인 카테고리가 이미 존재하고 있습니다.");
        }
    }

    @Test
    @DisplayName("카테고리 전체 조회 테스트")
    void findAllCategoriesSuccess() {
        categoryRepository.save(new Category("category1"));
        categoryRepository.save(new Category("category2"));

        FindAllCategoriesResponse findAllCategoriesResponse = categoryService.findAll();

        assertThat(findAllCategoriesResponse.categories()).hasSize(2);
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    void updateCategorySuccess() {
        //given
        Category savedCategory = categoryRepository.save(new Category("category1"));

        //when
        categoryService.update(savedCategory.getId(), new UpdateCategoryRequest("updateName"));

        //then
        assertThat(categoryRepository.fetchById(savedCategory.getId()).getName()).isEqualTo("updateName");
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategorySuccess() {
        //given
        Category savedCategory = categoryRepository.save(new Category("category1"));

        //when
        categoryService.deleteById(savedCategory.getId());

        //then
        assertThat(categoryRepository.findById(savedCategory.getId())).isEmpty();
    }
}
