package codezap.auth.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;

public class AuthorizationInterceptor implements HandlerInterceptor {
    private final CredentialManager credentialManager;
    private final CredentialProvider credentialProvider;

    public AuthorizationInterceptor(CredentialManager credentialManager, CredentialProvider credentialProvider) {
        this.credentialManager = credentialManager;
        this.credentialProvider = credentialProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = credentialManager.getCredential(request);
        validatedTokeIsBlank(token);
        return true;
    }

    private void validatedTokeIsBlank(String token) {
        if (token == null || token.isBlank()) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "회원의 인증 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
        credentialProvider.extractMember(token);
    }
}
