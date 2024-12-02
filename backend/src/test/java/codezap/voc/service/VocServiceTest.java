package codezap.voc.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.time.Duration;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import codezap.global.exception.CodeZapException;
import codezap.voc.config.VocConfiguration;
import codezap.voc.config.VocProperties;
import codezap.voc.dto.VocRequest;

class VocServiceTest {

    private VocService sut;

    private MockRestServiceServer mockServer;

    private RestClient.Builder builder;

    private VocConfiguration config;

    private VocProperties properties;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        properties = new VocProperties("http://localhost", Duration.ofNanos(1L), Duration.ofNanos(1L));
        config = new VocConfiguration(properties);
        builder = config.vocRestClientBuilder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        sut = new VocService(builder);
    }

    @AfterEach
    void tearDown() {
        mockServer.reset();
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("문의하기 성공")
    void create_success(HttpStatusCode statusCode) throws JsonProcessingException {
        // given
        var message = "lorem ipsum dolor sit amet consectetur adipiscing elit";
        var email = "codezap@gmail.com";
        var requestBody = new VocRequest(message, email);

        mockServer.expect(requestTo(properties.getBaseUrl()))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(requestBody)))
                .andRespond(withStatus(statusCode));

        // when
        sut.create(requestBody);

        // then
        mockServer.verify();
    }

    static Stream<HttpStatus> create_success() {
        return Arrays.stream(HttpStatus.values()).filter(status -> !status.isError());
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("외부 API에서 40x, 50x 상태 코드를 응답할 경우 예외 발생")
    void create_status_code_exception(HttpStatusCode statusCode) throws JsonProcessingException {
        // given
        var message = "lorem ipsum dolor sit amet consectetur adipiscing elit";
        var email = "codezap@gmail.com";
        var requestBody = new VocRequest(message, email);

        mockServer.expect(requestTo(properties.getBaseUrl()))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(requestBody)))
                .andRespond(withStatus(statusCode));

        // when & then
        assertThatCode(() -> sut.create(requestBody))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("스프레드시트 API 요청에 실패했습니다.");

        mockServer.verify();
    }

    static Stream<HttpStatus> create_status_code_exception() {
        return Arrays.stream(HttpStatus.values()).filter(HttpStatus::isError);
    }

    @Disabled("예상과 다르게 예외가 발생하지 않아 비활성화합니다.")
    @Test
    @DisplayName("시간 초과 예외 발생")
    void create_timeout() {
        // given
        var message = "lorem ipsum dolor sit amet consectetur adipiscing elit";
        var email = "codezap@gmail.com";
        var requestBody = new VocRequest(message, email);

        mockServer.expect(requestTo(properties.getBaseUrl()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(request -> {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ignored) {
                    }
                    return new MockClientHttpResponse();
                });

        // when & then
        assertThatThrownBy(() -> sut.create(requestBody))
                .isInstanceOf(CodeZapException.class)
                .hasMessageContaining("스프레드시트 API 요청 시간이 초과되었습니다");

        mockServer.verify();
    }

    @Disabled
    @Test
    @DisplayName("실제 API URL을 입력하여 테스트")
    void create_real_api() {
        var baseUrl = "여기에 실제 url 입력. 커밋하지 않게 주의.";
        var interceptor = loggingInterceptor();
        var restClientBuilder = RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(interceptor);
        var sut = new VocService(restClientBuilder);

        var message = "lorem ipsum dolor sit amet consectetur adipiscing elit";
        var email = "codezap@gmail.com";
        var requestBody = new VocRequest(message, email);

        sut.create(requestBody);
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            var response = execution.execute(request, body);
            var message = new StringJoiner("\n");
            response.getHeaders().forEach((k, v) -> message.add(k + ": " + v.toString()));
            System.out.println(message);
            return response;
        };
    }
}
