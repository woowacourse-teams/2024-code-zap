package codezap.performance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import codezap.auth.dto.request.LoginRequest;
import codezap.member.dto.request.SignupRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;

class AuthPerformanceTest extends MultipleRequestExecutor {

    @Test
    void 로그인() throws InterruptedException {
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);

        int statusCode = RestAssured.given()
                .body(new LoginRequest("code", "password1234"))
                .contentType(ContentType.JSON)
                .post("/login")
                .statusCode();
        assertThat(statusCode).withFailMessage("로그인 API 호출에 실패했습니다. 테스트 대상 서버가 실행중인지 확인해 주세요.").isEqualTo(200);

        executeMultipleRequests(requestCount, totalElapsedTime,
                () -> RestAssured.given()
                        .body(new SignupRequest("code", "password1234"))
                        .post("/members")
                        .statusCode());

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");

        assertThat(averageElapsedTime).isLessThanOrEqualTo(100L);
    }

    @Test
    void 로그인_상태_체크() throws InterruptedException {
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("credential", cookie)
                .get("/login/check")
                .statusCode();
        assertThat(statusCode).withFailMessage("로그인 API 호출에 실패했습니다. 테스트 대상 서버가 실행중인지 확인해 주세요.").isEqualTo(200);

        executeMultipleRequests(requestCount, totalElapsedTime,
                () -> RestAssured.given()
                        .contentType(ContentType.JSON)
                        .cookie("credential", cookie)
                        .get("/login/check")
                        .statusCode());

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");

        assertThat(averageElapsedTime).isLessThanOrEqualTo(100L);
    }

    @Test
    void 로그아웃() throws InterruptedException {
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("credential", cookie)
                .post("/logout")
                .statusCode();
        assertThat(statusCode).withFailMessage("로그인 API 호출에 실패했습니다. 테스트 대상 서버가 실행중인지 확인해 주세요.").isEqualTo(204);

        executeMultipleRequests(requestCount, totalElapsedTime,
                () -> RestAssured.given()
                        .contentType(ContentType.JSON)
                        .cookie("credential", cookie)
                        .post("/logout")
                        .statusCode());

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");

        assertThat(averageElapsedTime).isLessThanOrEqualTo(100L);
    }
}
