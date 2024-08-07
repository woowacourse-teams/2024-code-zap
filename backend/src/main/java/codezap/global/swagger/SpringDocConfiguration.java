package codezap.global.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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

        return new OpenAPI()
                .addServersItem(new Server().url("https://api.code-zap.com"))
                .info(info);
    }
}
