package codezap.global.swagger;

import java.util.List;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("코드잽 API")
                .version("v1.0")
                .description("""
                        코드잽 API 명세서입니다. \n
                        모든 예외 응답의 메시지는 "detail" : "내부 서버 오류입니다." 와 같은 형태로 응답합니다. \n
                        """);

        Components cookieComponent = new Components()
                .addSecuritySchemes("쿠키 인증 토큰", new SecurityScheme()
                        .type(Type.APIKEY)
                        .in(In.COOKIE)
                        .name("credential"));

        return new OpenAPI()
                .info(info)
                .components(cookieComponent)
                .servers(
                        List.of(
                                new Server()
                                        .url("http://localhost:8080")
                                        .description("로컬 서버"),
                                new Server()
                                        .url("https://dev.code-zap.com")
                                        .description("개발 서버")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("쿠키 인증 토큰"));
    }

    @Bean
    public OperationCustomizer addCommonErrorResponses() {
        return new AuthOperationCustomizer();
    }
}
