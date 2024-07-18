package codezap.global.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "코드잽 API", version = "v1"))
@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("코드잽 API")
                .version("v1.0")
                .description("코드잽 API 명세서입니다.");

        return new OpenAPI()
                .info(info);
    }
}
