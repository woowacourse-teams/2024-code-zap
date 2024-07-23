package codezap.template.controller;

import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
class TemplateControllerTest {

    private static final int MAX_LENGTH = 255;

    @LocalServerPort
    int port;

    @BeforeEach
    void setting() {
        RestAssured.port = port;
    }

    @ParameterizedTest
    @DisplayName("템플릿 생성 성공")
    @CsvSource({"a, 65535", "ㄱ, 21845"})
    void createTemplateSuccess(String repeatTarget, int maxLength) {
        String maxTitle = "a".repeat(MAX_LENGTH);
        CreateTemplateRequest templateRequest = new CreateTemplateRequest(maxTitle,
                List.of(new CreateSnippetRequest("a".repeat(MAX_LENGTH), repeatTarget.repeat(maxLength), 1)));
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
        CreateTemplateRequest templateRequest = new CreateTemplateRequest(exceededTitle,
                List.of(new CreateSnippetRequest("a", "content", 1)));
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
        CreateTemplateRequest templateRequest = new CreateTemplateRequest("title",
                List.of(new CreateSnippetRequest(exceededTitle, "content", 1)));
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
        CreateTemplateRequest templateRequest = new CreateTemplateRequest("title",
                List.of(new CreateSnippetRequest("title", repeatTarget.repeat(exceededLength), 1)));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(templateRequest)
                .when().post("/templates")
                .then().log().all()
                .statusCode(400)
                .body("detail", is("파일 내용은 최대 65,535 Byte까지 입력 가능합니다."));
    }

    @Test
    @DisplayName("템플릿 전체 조회 성공")
    void findAllTemplatesSuccess() {
        //given
        CreateTemplateRequest templateRequest1 = new CreateTemplateRequest("title1",
                List.of(new CreateSnippetRequest("filename", "content", 1)));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(templateRequest1)
                .when().post("/templates")
                .then().log().all();

        CreateTemplateRequest templateRequest2 = new CreateTemplateRequest("title2",
                List.of(new CreateSnippetRequest("filename", "content", 1)));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(templateRequest2)
                .when().post("/templates")
                .then().log().all();

        //when
        RestAssured.given().log().all()
                .get("/templates")
                .then().log().all()
                .statusCode(200)
                .body("templates.size()", is(2));
    }

    @Test
    @DisplayName("템플릿 상세 조회 성공")
    void findOneTemplateSuccess() {
        //given
        CreateTemplateRequest templateRequest = new CreateTemplateRequest("title",
                List.of(new CreateSnippetRequest("filename", "content", 1)));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(templateRequest)
                .when().post("/templates")
                .then().log().all();

        //when
        RestAssured.given().log().all()
                .get("/templates/1")
                .then().log().all()
                .statusCode(200)
                .body("title", is(templateRequest.title()),
                        "snippets.size()", is(1));
    }
}
