package codezap.global.cors;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationPropertiesScan
public class WebCorsConfiguration implements WebMvcConfigurer {

    private final CorsProperties corsProperties;

    public WebCorsConfiguration(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns(corsProperties.getAllowedOriginsPatterns())
                .allowedOrigins(corsProperties.getAllowedOrigins())
                .allowedMethods("*")
                .exposedHeaders("*");
    }
}
