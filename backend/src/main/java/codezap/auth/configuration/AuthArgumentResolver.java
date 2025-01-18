package codezap.auth.configuration;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import codezap.auth.dto.Credential;
import codezap.auth.manager.CredentialManagers;
import codezap.auth.provider.CredentialProvider;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final CredentialManagers credentialManagers;
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
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        if (isCredentialNecessary(parameter) && !hasCredential(request)) {
            return null;
        }
        return extractMember(request);
    }

    private boolean isCredentialNecessary(MethodParameter parameter) {
        AuthenticationPrinciple parameterAnnotation = parameter.getParameterAnnotation(AuthenticationPrinciple.class);
        boolean supported = Objects.nonNull(parameterAnnotation);
        return supported && !parameterAnnotation.required();
    }

    private boolean hasCredential(HttpServletRequest request) {
        return credentialManagers.hasCredential(request);
    }

    private Member extractMember(HttpServletRequest request) {
        Credential credential = credentialManagers.getCredential(request);
        return credentialProvider.extractMember(credential);
    }
}
