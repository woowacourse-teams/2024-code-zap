package codezap.voc.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Configuration
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class VocConfiguration {

    private final VocProperties properties;

    @Bean
    public RestClient.Builder restClientBuilder() {
        var httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(properties.getConnectTimeout());
        httpRequestFactory.setReadTimeout(properties.getReadTimeout());
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .requestFactory(httpRequestFactory);
    }
}
