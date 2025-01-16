package codezap.auth.configuration;

import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import codezap.auth.dto.Credential;
import codezap.auth.manager.CredentialManager;
import codezap.auth.manager.CredentialManagers;
import codezap.auth.provider.CredentialProvider;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final List<CredentialManager> credentialManagers;
    private final CredentialManagers credentialManagers1;
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
        boolean supported = Objects.nonNull(parameterAnnotation);
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        if (supported && !parameterAnnotation.required() && !hasCredential(request)) {
            return null;
        }
        CredentialManager credentialManager = credentialManagers.stream()
                .filter(cm -> cm.hasCredential(request))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증 정보가 없습니다. 다시 로그인해 주세요."));
        Credential credential = credentialManager.getCredential(request);
        return credentialProvider.extractMember(credential);
    }

    private boolean hasCredential(HttpServletRequest request) {
        return credentialManagers.stream()
                .anyMatch(cm -> cm.hasCredential(request));
    }
}
