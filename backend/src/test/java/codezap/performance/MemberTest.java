package codezap.performance;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import codezap.member.dto.request.SignupRequest;
import io.restassured.RestAssured;

class MemberTest {

    private static final String BASE_URI = "http://localhost:8080";
    private static final Long MIN_MEMBER_ID = 1L;
    private static final Long MAX_MEMBER_ID = 1L;
    private static final int THREAD_COUNT = 10;
    private static final int THREAD_REQUEST_COUNT = 100;
    private static final int TEST_DURATION_SECONDS = 60;
    private static final long MILLISECONDS_IN_SECOND = 1000L;
    public static final String COOKIE = "bmljb2xlODE6dmpPTzNERTRDaVJIOXdjdVNYVkhHQmZnVXhleFNCbHRaUDhMMnExTFAwWT0=";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("회원가입")
    void signup() throws InterruptedException {

        System.out.println("확인 api : 회원가입");
        System.out.println("---------------------------------------");

        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);
        byte[] bytes = new byte[7];
        new Random().nextBytes(bytes);

        // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
        int statusCode = RestAssured.given()
                .contentType("application/json")
                .body(new SignupRequest(new String(bytes, StandardCharsets.UTF_8), "aaaa1234"))
                .post("/signup")
                .statusCode();
        assertThat(statusCode).withFailMessage("회원가입 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.").isEqualTo(201);

        // 성능 테스트 시작
        executeMultipleRequests(requestCount, totalElapsedTime,
                () -> RestAssured.given()
                        .contentType("application/json")
                        .body(new SignupRequest(new String(bytes, StandardCharsets.UTF_8), "aaaa1234"))
                        .post("/signup"));

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
    }

    @Test
    @DisplayName("회원 정보 조회")
    void findMember() throws InterruptedException {
        System.out.println("확인 api : 회원 정보 조회");
        System.out.println("---------------------------------------");

        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);

        // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
        int statusCode = RestAssured.given()
                .cookie("credential", COOKIE)
                .get("/members/" + ThreadLocalRandom.current()
                        .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1)).statusCode();
        assertThat(statusCode)
                .withFailMessage("회원 정보 조회 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                .isEqualTo(200);

        // 성능 테스트 시작
        executeMultipleRequests(requestCount, totalElapsedTime, () ->
                RestAssured.given()
                        .cookie("credential", COOKIE)
                        .get("/members/" + ThreadLocalRandom.current()
                                .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1)));

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
    }

    @Test
    @DisplayName("사용자명 중복 확인")
    void checkUniquename() throws InterruptedException {
        System.out.println("확인 api : 사용자명 중복 확인");
        System.out.println("---------------------------------------");

        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);
        byte[] bytes = new byte[7];
        new Random().nextBytes(bytes);

        // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
        int statusCode = RestAssured.get("/check-name?name=" + new String(bytes, StandardCharsets.UTF_8)).statusCode();
        assertThat(statusCode)
                .withFailMessage("사용자명 중복 확인 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                .isEqualTo(200);

        // 성능 테스트 시작
        executeMultipleRequests(requestCount, totalElapsedTime, () ->
                RestAssured.get("/check-name?name=" + new String(bytes, StandardCharsets.UTF_8)));

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
    }

    private void executeMultipleRequests(
            AtomicInteger requestCount,
            AtomicLong totalElapsedTime,
            Runnable runnable
    ) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(() -> executeRequest(requestCount, totalElapsedTime, runnable));
        }

        // 1초 대기 후 실행 시작
        Thread.sleep(MILLISECONDS_IN_SECOND);

        executorService.shutdown();

        // 전체 테스트 시간 + 1초 동안 대기 후 강제 종료 시도
        if (!executorService.awaitTermination(TEST_DURATION_SECONDS + 1, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    private void executeRequest(
            AtomicInteger requestCount,
            AtomicLong totalElapsedTime,
            Runnable runnable
    ) {

        long elapsedTime = 0;
        for (int i = 0; i < THREAD_REQUEST_COUNT; i++) {
            long startTime = System.currentTimeMillis();
            runnable.run();
            long endTime = System.currentTimeMillis();

            elapsedTime += endTime - startTime;
            requestCount.incrementAndGet();

            try {
                Thread.sleep(10);  // 10ms 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        totalElapsedTime.addAndGet(elapsedTime);
    }
}
