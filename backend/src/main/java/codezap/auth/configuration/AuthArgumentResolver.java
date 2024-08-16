package codezap.auth.configuration;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final CredentialManager credentialManager;
    private final CredentialProvider credentialProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrinciple.class);
    }

    @Override
    public MemberDto resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String credential = credentialManager.getCredential(request);
        return MemberDto.from(credentialProvider.extractMember(credential));
    }
}
