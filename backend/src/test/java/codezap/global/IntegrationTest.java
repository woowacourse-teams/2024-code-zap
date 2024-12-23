package codezap.global;

import codezap.global.cors.CorsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableConfigurationProperties(CorsProperties.class)
public class IntegrationTest {

    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }
}
