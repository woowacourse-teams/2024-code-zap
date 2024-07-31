package codezap.template.controller;

import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.service.CategoryService;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSnippetRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.service.TemplateService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
class TemplateControllerTest {

    private static final int MAX_LENGTH = 255;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private CategoryService categoryService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setting() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("템플릿 생성 테스트")
    class createTemplateTest {

        @ParameterizedTest
        @DisplayName("템플릿 생성 성공")
        @CsvSource({"a, 65535", "ㄱ, 21845"})
        void createTemplateSuccess(String repeatTarget, int maxLength) {
            String maxTitle = "a".repeat(MAX_LENGTH);
            categoryService.create(new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(maxTitle,
                    List.of(new CreateSnippetRequest("a".repeat(MAX_LENGTH), repeatTarget.repeat(maxLength), 1)),
                    1L,
                    List.of("tag1", "tag2")
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(templateRequest)
                    .when().post("/templates")
                    .then().log().all()
                    .header("Location", "/templates/1")
                    .statusCode(201);
        }

        @Test
        @DisplayName("템플릿 생성 실패: 템플릿 이름 길이 초과")
        void createTemplateFailWithLongTitle() {
            String exceededTitle = "a".repeat(MAX_LENGTH + 1);
            categoryService.create(new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = new CreateTemplateRequest(exceededTitle,
                    List.of(new CreateSnippetRequest("a", "content", 1)),
                    1L,
                    List.of("tag1", "tag2")
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(templateRequest)
                    .when().post("/templates")
                    .then().log().all()
                    .statusCode(400)
                    .body("detail", is("템플릿 이름은 최대 255자까지 입력 가능합니다."));
        }

        @Test
        @DisplayName("템플릿 생성 실패: 파일 이름 길이 초과")
        void createTemplateFailWithLongFileName() {
            String exceededTitle = "a".repeat(MAX_LENGTH + 1);
            categoryService.create(new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = new CreateTemplateRequest("title",
                    List.of(new CreateSnippetRequest(exceededTitle, "content", 1)),
                    1L,
                    List.of("tag1", "tag2")
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(templateRequest)
                    .when().post("/templates")
                    .then().log().all()
                    .statusCode(400)
                    .body("detail", is("파일 이름은 최대 255자까지 입력 가능합니다."));
        }

        @ParameterizedTest
        @DisplayName("템플릿 생성 실패: 파일 내용 길이 초과")
        @CsvSource({"a, 65536", "ㄱ, 21846"})
        void createTemplateFailWithLongContent(String repeatTarget, int exceededLength) {
            categoryService.create(new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = new CreateTemplateRequest("title",
                    List.of(new CreateSnippetRequest("title", repeatTarget.repeat(exceededLength), 1)),
                    1L,
                    List.of("tag1", "tag2")
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(templateRequest)
                    .when().post("/templates")
                    .then().log().all()
                    .statusCode(400)
                    .body("detail", is("파일 내용은 최대 65,535 Byte까지 입력 가능합니다."));
        }

        @ParameterizedTest
        @DisplayName("템플릿 생성 실패: 잘못된 스니펫 순서 입력")
        @CsvSource({"0, 1", "1, 3", "2, 1"})
        void createTemplateFailWithWrongSnippetOrdinal(int firstIndex, int secondIndex) {
            categoryService.create(new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = new CreateTemplateRequest("title",
                    List.of(new CreateSnippetRequest("title", "content", firstIndex),
                            new CreateSnippetRequest("title", "content", secondIndex)),
                    1L,
                    List.of("tag1", "tag2")
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(templateRequest)
                    .when().post("/templates")
                    .then().log().all()
                    .statusCode(400)
                    .body("detail", is("스니펫 순서가 잘못되었습니다."));
        }
    }

    @Test
    @DisplayName("템플릿 전체 조회 성공")
    void findAllTemplatesSuccess() {
        // given
        CreateTemplateRequest templateRequest1 = createTemplateRequestWithTwoSnippets("title1");
        CreateTemplateRequest templateRequest2 = createTemplateRequestWithTwoSnippets("title2");
        categoryService.create(new CreateCategoryRequest("category"));
        templateService.create(templateRequest1);
        templateService.create(templateRequest2);

        // when & then
        RestAssured.given().log().all()
                .get("/templates")
                .then().log().all()
                .statusCode(200)
                .body("templates.size()", is(2));
    }

    @Nested
    @DisplayName("템플릿 상세 조회 테스트")
    class findTemplateTest {

        @Test
        @DisplayName("템플릿 상세 조회 성공")
        void findOneTemplateSuccess() {
            // given
            categoryService.create(new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSnippets("title");
            templateService.create(templateRequest);

            // when & then
            RestAssured.given().log().all()
                    .get("/templates/1")
                    .then().log().all()
                    .statusCode(200)
                    .body("title", is(templateRequest.title()),
                            "snippets.size()", is(2));
        }

        @Test
        @DisplayName("템플릿 상세 조회 실패: 존재하지 않는 템플릿 조회")
        void findOneTemplateFailWithNotFoundTemplate() {
            // when & then
            RestAssured.given().log().all()
                    .get("/templates/1")
                    .then().log().all()
                    .statusCode(404)
                    .body("detail", is("식별자 1에 해당하는 템플릿이 존재하지 않습니다."));
        }
    }

    @Nested
    @DisplayName("템플릿 수정 테스트")
    class updateTemplateTest {

        @Test
        @DisplayName("템플릿 수정 성공")
        void updateTemplateSuccess() {
            // given
            categoryService.create(new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSnippets("title");
            templateService.create(templateRequest);

            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "updateTitle",
                    List.of(
                            new CreateSnippetRequest("filename3", "content3", 2),
                            new CreateSnippetRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSnippetRequest(2L, "updateFilename2", "updateContent2", 1)
                    ),
                    List.of(1L)
            );

            // when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(updateTemplateRequest)
                    .when().post("/templates/1")
                    .then().log().all()
                    .statusCode(200);
        }

        // 정상 요청: 2, 3, 1
        @ParameterizedTest
        @DisplayName("템플릿 수정 실패: 잘못된 스니펫 순서 입력")
        @CsvSource({"1, 2, 1", "3, 2, 1", "0, 2, 1"})
        void updateTemplateFailWithWrongSnippetOrdinal(int createOrdinal1, int createOrdinal2, int updateOrdinal) {
            // given
            categoryService.create(new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSnippets("title");
            templateService.create(templateRequest);

            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "updateTitle",
                    List.of(
                            new CreateSnippetRequest("filename3", "content3", createOrdinal1),
                            new CreateSnippetRequest("filename4", "content4", createOrdinal2)
                    ),
                    List.of(
                            new UpdateSnippetRequest(2L, "updateFilename2", "updateContent2", updateOrdinal)
                    ),
                    List.of(1L)
            );

            // when & then
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(updateTemplateRequest)
                    .when().post("/templates/1")
                    .then().log().all()
                    .statusCode(400)
                    .body("detail", is("스니펫 순서가 잘못되었습니다."));
        }
    }

    @Nested
    @DisplayName("템플릿 삭제 테스트")
    class deleteTemplateTest {

        @Test
        @DisplayName("템플릿 삭제 성공")
        void deleteTemplateSuccess() {
            // given
            categoryService.create(new CreateCategoryRequest("category"));
            CreateTemplateRequest templateRequest = createTemplateRequestWithTwoSnippets("title");
            templateService.create(templateRequest);

            // when & then
            RestAssured.given().log().all()
                    .delete("/templates/1")
                    .then().log().all()
                    .statusCode(204);
        }

        @Test
        @DisplayName("템플릿 삭제 성공: 존재하지 않는 템플릿 삭제")
        void deleteTemplateSuccessWithNotFoundTemplate() {
            // when & then
            RestAssured.given().log().all()
                    .delete("/templates/1")
                    .then().log().all()
                    .statusCode(204);
        }

    }

    private static CreateTemplateRequest createTemplateRequestWithTwoSnippets(String title) {
        CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                title,
                List.of(
                        new CreateSnippetRequest("filename1", "content1", 1),
                        new CreateSnippetRequest("filename2", "content2", 2)
                ),
                1L,
                List.of("tag1", "tag2")
        );
        return templateRequest;
    }
}
