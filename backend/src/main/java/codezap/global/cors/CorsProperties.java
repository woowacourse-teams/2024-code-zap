package codezap.global.cors;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    private final String[] allowedOrigins;
    private final String[] allowedOriginsPatterns;

    public CorsProperties(
            @DefaultValue(value = "") String[] allowedOrigins,
            @DefaultValue(value = "") String[] allowedOriginsPatterns
    ) {
        this.allowedOrigins = allowedOrigins;
        this.allowedOriginsPatterns = allowedOriginsPatterns;
    }

    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }

    public String[] getAllowedOriginsPatterns() {
        return allowedOriginsPatterns;
    }
}
