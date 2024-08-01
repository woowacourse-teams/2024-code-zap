package codezap.category.controller;

import static org.hamcrest.Matchers.is;

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

import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.service.CategoryService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
class CategoryControllerTest {

    private static final int MAX_LENGTH = 255;

    @LocalServerPort
    int port;

    @Autowired
    private CategoryService categoryService;

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
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("a".repeat(MAX_LENGTH));

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(createCategoryRequest)
                    .when().post("/categories")
                    .then().log().all()
                    .header("Location", "/categories/1")
                    .statusCode(201);
        }

        @Test
        @DisplayName("카테고리 생성 실패:")
        void createCategoryFail() {
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("a".repeat(MAX_LENGTH + 1));

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(createCategoryRequest)
                    .when().post("/categories")
                    .then().log().all()
                    .statusCode(400)
                    .body("detail", is("카테고리 이름은 최대 255자까지 입력 가능합니다."));
        }
    }

    @Test
    @DisplayName("카테고리 전체 조회 성공")
    void findAllCategoriesSuccess() {
        CreateCategoryRequest createCategoryRequest1 = new CreateCategoryRequest("category1");
        CreateCategoryRequest createCategoryRequest2 = new CreateCategoryRequest("category2");
        categoryService.create(createCategoryRequest1);
        categoryService.create(createCategoryRequest2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/categories")
                .then().log().all()
                .statusCode(200)
                .body("categories.size()", is(2));
    }

    @Nested
    @DisplayName("카테고리 수정 테스트")
    class updateCategoryTest {

        Long savedCategoryId;

        @BeforeEach
        void saveCategory() {
            savedCategoryId = categoryService.create(new CreateCategoryRequest("category1"));
        }

        @Test
        @DisplayName("카테고리 수정 성공")
        void updateCategorySuccess() {
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("a".repeat(MAX_LENGTH));

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(updateCategoryRequest)
                    .when().put("/categories/" + savedCategoryId)
                    .then().log().all()
                    .statusCode(200);
        }

        @Test
        @DisplayName("카테고리 수정 실패: 카테고리 이름 길이 초과")
        void updateCategoryFailWithLongName() {
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest("a".repeat(MAX_LENGTH + 1));

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(updateCategoryRequest)
                    .when().put("/categories/" + savedCategoryId)
                    .then().log().all()
                    .statusCode(400)
                    .body("detail", is("카테고리 이름은 최대 255자까지 입력 가능합니다."));
        }

        @Test
        @DisplayName("카테고리 수정 실패: 중복된 이름의 카테고리 존재")
        void updateCategoryFailWithDuplicatedName() {
            //given
            String duplicatedName = "duplicatedName";
            categoryService.create(new CreateCategoryRequest(duplicatedName));

            UpdateCategoryRequest createCategoryRequest = new UpdateCategoryRequest(duplicatedName);

            //when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(createCategoryRequest)
                    .when().put("/categories/" + savedCategoryId)
                    .then().log().all()
                    .statusCode(409)
                    .body("detail", is("이름이 " + duplicatedName + "인 카테고리가 이미 존재하고 있습니다."));
        }
    }
}
