package codezap.template;

import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TemplateIntegrationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setting() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("템플릿 생성 성공")
    void createTemplateSuccess() {
        CreateTemplateRequest templateRequest = new CreateTemplateRequest("a".repeat(255),
                List.of(new CreateSnippetRequest("a".repeat(255), "content", 1)));
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
        CreateTemplateRequest templateRequest = new CreateTemplateRequest("a".repeat(256),
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
        CreateTemplateRequest templateRequest = new CreateTemplateRequest("title",
                List.of(new CreateSnippetRequest("a".repeat(256), "content", 1)));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(templateRequest)
                .when().post("/templates")
                .then().log().all()
                .statusCode(400)
                .body("detail", is("파일 이름은 최대 255자까지 입력 가능합니다."));
    }
}
