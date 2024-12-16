package codezap.voc.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties("voc.http.client")
@RequiredArgsConstructor
@Getter
public class VocProperties {
    private final String baseUrl;
    private final Duration connectTimeout;
    private final Duration readTimeout;
}
