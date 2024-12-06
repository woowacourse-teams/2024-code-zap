package codezap.auth.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthWebConfiguration implements WebMvcConfigurer {


    private final CredentialManager credentialManager;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(credentialManager));
    }
}
