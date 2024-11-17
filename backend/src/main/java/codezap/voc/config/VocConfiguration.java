package codezap.voc.config;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableConfigurationProperties(VocProperties.class)
@RequiredArgsConstructor
@Slf4j
public class VocConfiguration {

    private final VocProperties properties;

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultStatusHandler(new VocResponseErrorHandler())
                .requestInterceptor(new VocClientHttpRequestInterceptor())
                .requestFactory(requestFactory());
    }

    private ClientHttpRequestFactory requestFactory() {
        var httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(properties.getConnectTimeout());
        httpRequestFactory.setReadTimeout(properties.getReadTimeout());
        return new BufferingClientHttpRequestFactory(httpRequestFactory);
    }
}
