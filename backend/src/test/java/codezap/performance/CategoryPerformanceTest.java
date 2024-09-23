package codezap.performance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;

import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

class CategoryPerformanceTest extends MultipleRequestExecutor {

    @Test
    void 카테고리_생성() throws InterruptedException {
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);

        int statusCode = RestAssured.given()
                .body(new CreateCategoryRequest("category" + requestCount.get()))
                .cookie("credential", cookie)
                .contentType(ContentType.JSON)
                .post("/categories")
                .statusCode();
        assertThat(statusCode).withFailMessage("카테고리 API 호출에 실패했습니다. 테스트 대상 서버가 실행중인지 확인해 주세요.").isEqualTo(201);

        executeMultipleRequests(requestCount, totalElapsedTime,
                () -> RestAssured.given()
                        .cookie("credential", cookie)
                        .body(new CreateCategoryRequest("category" + requestCount.get()))
                        .contentType(ContentType.JSON)
                        .post("/categories")
                        .statusCode());

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");

        assertThat(averageElapsedTime).isLessThanOrEqualTo(100L);
    }

    @Test
    void 회원의_카테고리_목록_조회() throws InterruptedException {
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .get("/categories?memberId=2")
                .statusCode();
        assertThat(statusCode).withFailMessage("로그인 API 호출에 실패했습니다. 테스트 대상 서버가 실행중인지 확인해 주세요.").isEqualTo(200);

        executeMultipleRequests(requestCount, totalElapsedTime,
                () -> RestAssured.given()
                        .cookie("credential", cookie)
                        .get("/categories?memberId=2")
                        .statusCode());

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");

        assertThat(averageElapsedTime).isLessThanOrEqualTo(100L);
    }

    @Test
    void 카테고리_업데이트() throws InterruptedException {
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);

        int statusCode = RestAssured.given()
                .body(new UpdateCategoryRequest("code12" + requestCount.get()))
                .cookie("credential", cookie)
                .contentType(ContentType.JSON)
                .put("/categories/24054")
                .statusCode();
        assertThat(statusCode).withFailMessage("카테고리 업데이트 API 호출에 실패했습니다. 테스트 대상 서버가 실행중인지 확인해 주세요.").isEqualTo(200);

        executeMultipleRequests(requestCount, totalElapsedTime,
                () -> RestAssured.given()
                        .body(new UpdateCategoryRequest("code12" + requestCount.get()))
                        .cookie("credential", cookie)
                        .contentType(ContentType.JSON)
                        .put("/categories/24054")
                        .statusCode());

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");

        assertThat(averageElapsedTime).isLessThanOrEqualTo(100L);
    }

    @Test
    void 카테고리_삭제() throws InterruptedException {
        AtomicInteger requestCount = new AtomicInteger(24052);
        AtomicLong totalElapsedTime = new AtomicLong(0);

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("credential", cookie)
                .delete("/categories/" + requestCount.get())
                .statusCode();
        assertThat(statusCode).withFailMessage("카테고리 삭제 API 호출에 실패했습니다. 테스트 대상 서버가 실행중인지 확인해 주세요.").isEqualTo(204);

        executeMultipleRequests(requestCount, totalElapsedTime,
                () -> RestAssured.given()
                        .contentType(ContentType.JSON)
                        .cookie("credential", cookie)
                        .delete("/categories/" + requestCount.get()));

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");

        assertThat(averageElapsedTime).isLessThanOrEqualTo(100L);
    }
}
