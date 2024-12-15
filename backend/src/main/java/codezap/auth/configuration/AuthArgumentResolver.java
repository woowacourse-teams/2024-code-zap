package codezap.auth.configuration;

import codezap.auth.dto.Credential;
import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final CredentialManager credentialManager;
    private final CredentialProvider credentialProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrinciple.class);
    }

    @Override
    public Member resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        AuthenticationPrinciple parameterAnnotation = parameter.getParameterAnnotation(AuthenticationPrinciple.class);
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        if (!parameterAnnotation.required() && !credentialManager.hasCredential(request)) {
            return null;
        }
        Credential credential = credentialManager.getCredential(request);
        return credentialProvider.extractMember(credential);
    }
}
