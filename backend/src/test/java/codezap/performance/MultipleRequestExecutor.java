package codezap.performance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import codezap.auth.dto.request.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

class MultipleRequestExecutor {

    protected static final String BASE_URI = "http://localhost:8080";
    protected static final int THREAD_COUNT = 10;
    protected static final long MILLISECONDS_IN_SECOND = 1000L;
    protected static final long REQUESTS_PER_THREAD = 100L;

    protected String cookie;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    void getCookie() {
        cookie = RestAssured.given()
                .body(new LoginRequest("code", "password1234"))
                .contentType(ContentType.JSON)
                .post("/signup")
                .getCookie("credential");

        cookie = RestAssured.given()
                .body(new LoginRequest("code", "password1234"))
                .contentType(ContentType.JSON)
                .post("/login")
                .getCookie("credential");
    }

    public void executeMultipleRequests(
            AtomicInteger requestCount, AtomicLong totalElapsedTime, Runnable runnable
    )
            throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(() -> executeRequest(requestCount, totalElapsedTime, runnable));
        }

        Thread.sleep(MILLISECONDS_IN_SECOND);    // 스레드에 실행 요청 후 1초간 대기한 후 요청을 시작하도록 변경한다.

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);
    }

    private void executeRequest(
            AtomicInteger requestCount,
            AtomicLong totalElapsedTime,
            Runnable runnable
    ) {
        long elapsedTime = 0;
        for (int i = 0; i < REQUESTS_PER_THREAD; i++) {
            long startTime = System.currentTimeMillis();
            runnable.run();
            long endTime = System.currentTimeMillis();

            elapsedTime += endTime - startTime;
            requestCount.incrementAndGet();
        }

        totalElapsedTime.addAndGet(elapsedTime);
    }
}
