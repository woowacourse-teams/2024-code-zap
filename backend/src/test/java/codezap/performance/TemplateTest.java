package codezap.performance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import io.restassured.RestAssured;

class TemplateTest {
    private static final String BASE_URI = "http://localhost:8080";
    private static final Long MIN_TEMPLATE_ID = 1L;
    private static final Long MAX_TEMPLATE_ID = 100000L;
    private static final Long MIN_MEMBER_ID = 1L;
    private static final Long MAX_MEMBER_ID = 1L;
    private static final Long MIN_CATEGORY_ID = 1L;
    private static final Long MAX_CATEGORY_ID = 5L;
    private static final Long MIN_TAG_ID = 1L;
    private static final Long MAX_TAG_ID = 1000L;
    private static final int THREAD_COUNT = 10;
    private static final int[] THREAD_REQUEST_COUNT = new int[]{100};
    private static final int TEST_DURATION_SECONDS = 60;
    private static final long MILLISECONDS_IN_SECOND = 1000L;
    public static final String COOKIE = "bmljb2xlODE6VDBqNDZoK1dzNVB6UzFIZjJ0cXpsZ0Y4dUJJZjdaaHRuOERoMHU1Q29MYz0=";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("템플릿 생성")
    void createTemplate() throws InterruptedException {

        System.out.println("확인 api : 템플릿 생성");
        System.out.println("---------------------------------------");

        CreateTemplateRequest templateRequest = new CreateTemplateRequest(
                "title",
                "description",
                List.of(new CreateSourceCodeRequest("title", "content", 1)),
                1,
                1L,
                List.of("tag1")
        );

        // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
        int statusCode = RestAssured.given()
                .cookie("credential", COOKIE)
                .contentType("application/json")
                .body(templateRequest)
                .post("/templates")
                .statusCode();
        assertThat(statusCode).withFailMessage("템플릿 생성 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.").isEqualTo(201);

        for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
            AtomicInteger requestCount = new AtomicInteger(0);
            AtomicLong totalElapsedTime = new AtomicLong(0);

            // 성능 테스트 시작
            executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                    () -> RestAssured.given()
                            .cookie("credential", COOKIE)
                            .contentType("application/json")
                            .body(templateRequest)
                            .post("/templates"));

            System.out.println("Total request count: " + requestCount.get());
            System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

            long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
            System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
            System.out.println("----------------------------------------");

        }

    }

    @Nested
    @DisplayName("템플릿 검색")
    class GetTemplates {

        @BeforeEach
        void setUp() {
            System.out.println("확인 api : 템플릿 검색");
            System.out.println("---------------------------------------");
        }

        @Nested
        @DisplayName("템플릿 검색 1개 조합")
        class GetTemplatesOneFilter {
            @Test
            @DisplayName("템플릿 검색 : 멤버만")
            void getTemplatesWithMember() throws InterruptedException {
                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);

                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");

                }
            }

            @Test
            @DisplayName("템플릿 검색 : 키워드만")
            void getTemplatesWithKeyword() throws InterruptedException {
                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?keyword=" + ThreadLocalRandom.current()).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);
                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?keyword=" + ThreadLocalRandom.current()));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 카테고리만")
            void getTemplatesWithCategory() throws InterruptedException {
                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?categoryId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);
                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?categoryId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 태그만")
            void getTemplatesWithTags() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?tagIds=" + ThreadLocalRandom.current()
                        .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);

                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?tagIds=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }
        }

        @Nested
        @DisplayName("템플릿 검색 2개 조합")
        class GetTemplatesTwoFilter {
            @Test
            @DisplayName("템플릿 검색 : 멤버, 키워드")
            void getTemplatesWithMemberKeyword() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                        "&keyword=" + ThreadLocalRandom.current()).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);

                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                                    "&keyword=" + ThreadLocalRandom.current()));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 멤버, 카테고리")
            void getTemplatesWithMemberCategory() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                        "&categoryId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);

                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                                    "&categoryId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 멤버, 태그")
            void getTemplatesWithMemberTag() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                        "&tagIds=" + ThreadLocalRandom.current()
                        .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);

                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                                    "&tagIds=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 키워드, 카테고리")
            void getTemplatesWithKeywordCategory() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?keyword=" + ThreadLocalRandom.current() +
                        "&categoryId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);

                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?keyword=" + ThreadLocalRandom.current() +
                                    "&categoryId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 키워드, 태그")
            void getTemplatesWithKeywordTag() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?keyword=" + ThreadLocalRandom.current() +
                        "&tagIds=" + ThreadLocalRandom.current()
                        .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);

                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?keyword=" + ThreadLocalRandom.current() +
                                    "&tagIds=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 카테고리, 태그")
            void getTemplatesWithCategoryTag() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?categoryId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1) +
                        "&tagIds=" + ThreadLocalRandom.current()
                        .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);

                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?categoryId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1) +
                                    "&tagIds=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }
        }

        @Nested
        @DisplayName("템플릿 검색 3개 조합")
        class GetTemplatesThreeFilter {
            @Test
            @DisplayName("템플릿 검색 : 멤버, 키워드, 카테고리")
            void getTemplatesWithMemberKeywordCategory() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                        "&keyword=" + ThreadLocalRandom.current() +
                        "&categoryId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);
                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                                    "&keyword=" + ThreadLocalRandom.current() +
                                    "&categoryId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 멤버, 키워드, 태그")
            void getTemplatesWithMemberKeywordTag() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                        "&keyword=" + ThreadLocalRandom.current() +
                        "&tagIds=" + ThreadLocalRandom.current()
                        .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);
                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                                    "&keyword=" + ThreadLocalRandom.current() +
                                    "&tagIds=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 멤버, 카테고리, 태그")
            void getTemplatesWithMemberCategoryTag() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                        "&categoryId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1) +
                        "&tagIds=" + ThreadLocalRandom.current()
                        .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);
                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                                    "&categoryId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1) +
                                    "&tagIds=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }

            @Test
            @DisplayName("템플릿 검색 : 키워드, 카테고리, 태그")
            void getTemplatesWithKeywordCategoryTag() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates/keyword=" + ThreadLocalRandom.current() +
                        "&categoryId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1) +
                        "&tagIds=" + ThreadLocalRandom.current()
                        .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);
                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates/keyword=" + ThreadLocalRandom.current() +
                                    "&categoryId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1) +
                                    "&tagIds=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }
        }

        @Nested
        @DisplayName("템플릿 검색 4개 조합")
        class GetTemplatesFourFilter {
            @Test
            @DisplayName("템플릿 검색 : 멤버, 키워드, 카테고리, 태그")
            void getTemplatesWithMemberKeywordCategoryTag() throws InterruptedException {

                // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
                int statusCode = RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                        "&keyword=" + ThreadLocalRandom.current() +
                        "&categoryId=" + ThreadLocalRandom.current()
                        .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1) +
                        "&tagIds=" + ThreadLocalRandom.current()
                        .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)).statusCode();
                assertThat(statusCode)
                        .withFailMessage("템플릿 검색 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                        .isEqualTo(200);

                for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
                    AtomicInteger requestCount = new AtomicInteger(0);
                    AtomicLong totalElapsedTime = new AtomicLong(0);
                    // 성능 테스트 시작
                    executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                            () -> RestAssured.get("/templates?memberId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_MEMBER_ID, MAX_MEMBER_ID + 1) +
                                    "&keyword=" + ThreadLocalRandom.current() +
                                    "&categoryId=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_CATEGORY_ID, MAX_CATEGORY_ID + 1) +
                                    "&tagIds=" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TAG_ID, MAX_TAG_ID + 1)));

                    System.out.println("Total request count: " + requestCount.get());
                    System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

                    long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
                    System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
                    System.out.println("----------------------------------------");
                }
            }
        }
    }

    @Test
    @DisplayName("템플릿 단건 조회")
    void getTemplateById() throws InterruptedException {
        System.out.println("확인 api : 템플릿 단건 조회");
        System.out.println("---------------------------------------");

        // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
        int statusCode = RestAssured.get("/templates/" + ThreadLocalRandom.current()
                .nextLong(MIN_TEMPLATE_ID, MAX_TEMPLATE_ID + 1)).statusCode();
        assertThat(statusCode)
                .withFailMessage("템플릿 단건 조회 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.")
                .isEqualTo(200);

        for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
            AtomicInteger requestCount = new AtomicInteger(0);
            AtomicLong totalElapsedTime = new AtomicLong(0);
            // 성능 테스트 시작
            executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                    () -> RestAssured.get("/templates/" + ThreadLocalRandom.current()
                            .nextLong(MIN_TEMPLATE_ID, MAX_TEMPLATE_ID + 1)));

            System.out.println("Total request count: " + requestCount.get());
            System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

            long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
            System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
            System.out.println("----------------------------------------");
        }
    }

    @Test
    @DisplayName("템플릿 수정")
    void updateTemplate() throws InterruptedException {
        System.out.println("확인 api : 템플릿 수정");
        System.out.println("---------------------------------------");

        UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                "updateTitle",
                "description",
                List.of(
                        new CreateSourceCodeRequest("filename3", "content3", 1)
                ),
                List.of(),
                List.of(),
                1L,
                List.of("tag1", "tag3")
        );

        // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
        int statusCode = RestAssured.given()
                .cookie("credential", COOKIE)
                .contentType("application/json")
                .body(updateTemplateRequest)
                .post("/templates/" + ThreadLocalRandom.current()
                        .nextLong(MIN_TEMPLATE_ID, MAX_TEMPLATE_ID + 1))
                .statusCode();
        assertThat(statusCode).withFailMessage("템플릿 수정 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.").isEqualTo(200);

        for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
            AtomicInteger requestCount = new AtomicInteger(0);
            AtomicLong totalElapsedTime = new AtomicLong(0);

            // 성능 테스트 시작
            executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                    () -> RestAssured.given()
                            .cookie("credential", COOKIE)
                            .contentType("application/json")
                            .body(updateTemplateRequest)
                            .post("/templates/" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TEMPLATE_ID, MAX_TEMPLATE_ID + 1)));

            System.out.println("Total request count: " + requestCount.get());
            System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

            long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
            System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
            System.out.println("----------------------------------------");
        }
    }

    @Test
    @DisplayName("템플릿 삭제")
    void deleteTemplates() throws InterruptedException {
        System.out.println("확인 api : 템플릿 삭제");
        System.out.println("---------------------------------------");


        // 서버가 정상적으로 동작하는지 확인 (성능 테스트 전 확인용)
        int statusCode = RestAssured.given()
                .cookie("credential", COOKIE)
                .delete("/templates/" + ThreadLocalRandom.current()
                        .nextLong(MIN_TEMPLATE_ID, MAX_TEMPLATE_ID + 1))
                .statusCode();
        assertThat(statusCode).withFailMessage("템플릿 삭제 API 호출에 실패했습니다. 테스트 대상 서버가 실행 중인지 확인해 주세요.").isEqualTo(204);

        for (int i = 0; i < THREAD_REQUEST_COUNT.length; i++) {
            AtomicInteger requestCount = new AtomicInteger(0);
            AtomicLong totalElapsedTime = new AtomicLong(0);
            // 성능 테스트 시작
            executeMultipleRequests(requestCount, totalElapsedTime, THREAD_REQUEST_COUNT[i],
                    () -> RestAssured.given()
                            .cookie("credential", COOKIE)
                            .delete("/templates/" + ThreadLocalRandom.current()
                                    .nextLong(MIN_TEMPLATE_ID, MAX_TEMPLATE_ID + 1)));

            System.out.println("Total request count: " + requestCount.get());
            System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

            long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
            System.out.println("Average elapsed time: " + averageElapsedTime + "ms");
            System.out.println("----------------------------------------");
        }
    }


    private void executeMultipleRequests(
            AtomicInteger requestCount,
            AtomicLong totalElapsedTime,
            long testCount,
            Runnable runnable
    ) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(() -> executeRequest(requestCount, totalElapsedTime, runnable, testCount));
        }

        // 1초 대기 후 실행 시작
        Thread.sleep(MILLISECONDS_IN_SECOND);

        executorService.shutdown();

        // 전체 테스트 시간 + 1초 동안 대기 후 강제 종료 시도
        if (!executorService.awaitTermination(TEST_DURATION_SECONDS, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    private void executeRequest(
            AtomicInteger requestCount,
            AtomicLong totalElapsedTime,
            Runnable runnable,
            long testCount
    ) {

        long elapsedTime = 0;
        for (int i = 0; i < testCount; i++) {
            long startTime = System.currentTimeMillis();
            runnable.run();
            long endTime = System.currentTimeMillis();

            elapsedTime += endTime - startTime;
            requestCount.incrementAndGet();

            // 요청 사이의 간격 추가 (과도한 요청 방지)
            try {
                Thread.sleep(10);  // 10ms 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        totalElapsedTime.addAndGet(elapsedTime);
    }
}
