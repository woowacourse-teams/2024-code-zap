package codezap.category.controller;

import static org.hamcrest.Matchers.is;

import java.util.List;

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
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.service.TemplateService;
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
    @Autowired
    private TemplateService templateService;

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
        @DisplayName("카테고리 생성 실패: 카테고리 이름 길이 초과")
        void createCategoryFailWithLongName() {
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


    @Nested
    @DisplayName("카테고리 삭제 테스트")
    class deleteCategoryTest {

        Long savedCategoryId;

        @BeforeEach
        void saveCategory() {
            savedCategoryId = categoryService.create(new CreateCategoryRequest("category1"));
        }

        @Test
        @DisplayName("카테고리 삭제 성공")
        void deleteCategorySuccess() {
            RestAssured.given().log().all()
                    .when().delete("/categories/" + savedCategoryId)
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        @DisplayName("카테고리 수정 성공: 존재하지 않는 카테고리의 삭제 요청")
        void updateCategoryFailWithDuplicatedName() {
            RestAssured.given().log().all()
                    .when().delete("/categories/" + savedCategoryId + 1)
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        @DisplayName("카테고리 삭제 실패: 템플릿이 존재하는 카테고리는 삭제 불가능")
        void updateCategoryFailWithLongName() {
            //given
            templateService.create(new CreateTemplateRequest(
                    "title",
                    List.of(new CreateSnippetRequest("filename", "content", 1)),
                    savedCategoryId,
                    List.of("tag1", "tag2")
            ));

            //when & then
            RestAssured.given().log().all()
                    .when().delete("/categories/" + savedCategoryId)
                    .then().log().all()
                    .statusCode(400)
                    .body("detail", is("템플릿이 존재하는 카테고리는 삭제할 수 없습니다."));
        }
    }
}
