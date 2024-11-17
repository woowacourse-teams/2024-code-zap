package codezap.voc.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = VocConfiguration.class)
class VocPropertiesTest {

    @Autowired
    private VocProperties sut;

    @Test
    @DisplayName("yml 파일로부터 http client 관련 설정 값을 가져오는지 확인")
    void getAllowedOrigins() {
        assertAll(
                () -> assertThat(sut.getBaseUrl()).isEqualTo("localhost:8080"),
                () -> assertThat(sut.getConnectTimeout()).isEqualTo(Duration.ofSeconds(5L)),
                () -> assertThat(sut.getReadTimeout()).isEqualTo(Duration.ofSeconds(5L))
        );
    }
}
