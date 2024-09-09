package codezap.global.cors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
class CorsPropertiesTest {

    @Autowired
    private CorsProperties corsProperties;

    @Test
    @DisplayName("yml 파일로부터 allowed-origins 값을 가져오는지 확인")
    void getAllowedOrigins() {
        assertThat(corsProperties.getAllowedOrigins()).isEqualTo(new String[]{"http://localhost:3000"});
    }

    @Test
    @DisplayName("yml 파일로부터 allowed-origins-patterns 값을 가져오는지 확인")
    void getAllowedOriginsPatterns() {
        assertThat(corsProperties.getAllowedOriginsPatterns()).isEqualTo(new String[]{""});
    }
}
