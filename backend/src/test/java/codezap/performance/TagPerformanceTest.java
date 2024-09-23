package codezap.performance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

class TagPerformanceTest extends MultipleRequestExecutor {

    @Test
    void 회원의_전체_태그_목록_조회() throws InterruptedException {
        AtomicInteger requestCount = new AtomicInteger(0);
        AtomicLong totalElapsedTime = new AtomicLong(0);

        int statusCode = RestAssured.get("tags?memberId=1").statusCode();
        assertThat(statusCode).withFailMessage("쿠폰의 발급 수량 조회 API 호출에 실패했습니다. 테스트 대상 서버가 실행중인지 확인해 주세요.").isEqualTo(200);

        executeMultipleRequests(requestCount, totalElapsedTime,
                () -> RestAssured.get("tags?memberId=1").statusCode());

        System.out.println("Total request count: " + requestCount.get());
        System.out.println("Total elapsed time: " + totalElapsedTime.get() + "ms");

        long averageElapsedTime = totalElapsedTime.get() / requestCount.get();
        System.out.println("Average elapsed time: " + averageElapsedTime + "ms");

        assertThat(averageElapsedTime).isLessThanOrEqualTo(100L);
    }
}
