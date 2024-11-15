package codezap.voc.service;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import codezap.voc.dto.VocRequest;

class VocServiceTest {

    private VocService sut;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(1L));
        requestFactory.setReadTimeout(Duration.ofSeconds(1L));

        var restClientBuilder = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .requestFactory(requestFactory);

        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        sut = new VocService(restClientBuilder);
    }

    @Test
    @DisplayName("문의하기 성공")
    void contact_success() {
        // given
        mockServer.expect(requestTo("http://localhost:8080/spreadsheet/contact"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        // when
        var message = "lorem ipsum dolor sit amet consectetur adipiscing elit";
        var email = "codezap@gmail.com";
        var request = new VocRequest(message, email);
        sut.contact(request);

        // then
        mockServer.verify();
    }

    // TODO: 예외 테스트

    // TODO: 삭제
    @Disabled
    @Test
    @DisplayName("실제 경로를 입력하여 구글 스프레드시트 API 테스트")
    void contact_real_api() {
        var baseUrl = "여기에 실제 경로를 입력하세요. 커밋하지 않게 주의.";
        var restClientBuilder = RestClient.builder()
                .baseUrl(baseUrl);

        sut = new VocService(restClientBuilder);

        var message = "lorem ipsum dolor sit amet consectetur adipiscing elit";
        var email = "codezap@gmail.com";
        var request = new VocRequest(message, email);
        sut.contact(request);
    }
}
