package codezap.voc.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.time.Duration;
import java.util.Arrays;
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
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import codezap.global.exception.CodeZapException;
import codezap.voc.config.VocClientHttpRequestInterceptor;
import codezap.voc.config.VocProperties;
import codezap.voc.config.VocResponseErrorHandler;
import codezap.voc.dto.VocRequest;

class VocServiceTest {

    private VocService sut;

    private VocProperties properties;

    private MockRestServiceServer mockServer;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        properties = new VocProperties("http://localhost", Duration.ofSeconds(5), Duration.ofSeconds(5));
        var builder = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultStatusHandler(new VocResponseErrorHandler())
                .requestInterceptor(new VocClientHttpRequestInterceptor());
        mockServer = MockRestServiceServer.bindTo(builder).build();
        sut = new VocService(builder);
    }

    @AfterEach
    void tearDown() {
        mockServer.reset();
    }

    @Test
    @DisplayName("문의하기 성공")
    void contact_success() throws JsonProcessingException {
        // given
        var message = "lorem ipsum dolor sit amet consectetur adipiscing elit";
        var email = "codezap@gmail.com";
        var request = new VocRequest(message, email);

        mockServer.expect(requestTo(properties.getBaseUrl()))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(request)))
                .andRespond(withStatus(HttpStatus.CREATED));

        // when
        sut.contact(request);

        // then
        mockServer.verify();
    }

    // ResponseErrorHandler가 동작하지 않아요. 그래서 테스트에 자꾸 실패해요. 어떻게 동작시킬까요?
    @ParameterizedTest
    @MethodSource
    @DisplayName("외부 API에서 40x, 50x 상태 코드를 응답할 경우 예외 발생")
    void defaultStatusHandler(HttpStatusCode statusCode) throws JsonProcessingException {
        // given
        var message = "lorem ipsum dolor sit amet consectetur adipiscing elit";
        var email = "codezap@gmail.com";
        var request = new VocRequest(message, email);

        mockServer.expect(requestTo(properties.getBaseUrl()))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(request)))
                .andRespond(withStatus(statusCode));

        // when & then
        assertThatCode(() -> sut.contact(request))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("스프레드시트 API 요청에 실패했습니다.");

        mockServer.verify();
    }

    static Stream<HttpStatus> defaultStatusHandler() {
        return Arrays.stream(HttpStatus.values()).filter(HttpStatus::isError);
    }

    @Disabled
    @Test
    @DisplayName("실제 경로를 입력하여 구글 sheets API 테스트")
    void contact_real_api() {
        var baseUrl = "여기에 실제 경로 입력. 커밋하지 않게 주의.";

        var restClientBuilder = RestClient.builder().baseUrl(baseUrl);
        var sut = new VocService(restClientBuilder);

        var message = "lorem ipsum dolor sit amet consectetur adipiscing elit";
        var email = "codezap@gmail.com";
        var request = new VocRequest(message, email);

        sut.contact(request);
    }
}
