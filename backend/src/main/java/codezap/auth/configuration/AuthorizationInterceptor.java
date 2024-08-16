package codezap.auth.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.global.exception.CodeZapException;

public class AuthorizationInterceptor implements HandlerInterceptor {
    private final CredentialManager credentialManager;
    private final CredentialProvider credentialProvider;

    public AuthorizationInterceptor(CredentialManager credentialManager, CredentialProvider credentialProvider) {
        this.credentialManager = credentialManager;
        this.credentialProvider = credentialProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler
    ) {
        String token = credentialManager.getCredential(request);
        validatedTokeIsBlank(token);
        validatedExistMember(token);
        return true;
    }

    private void validatedTokeIsBlank(final String token) {
        if (token == null || token.isBlank()) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "회원의 인증 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
    }

    private void validatedExistMember(final String token) {
        try {
            credentialProvider.extractMember(token);
        } catch (CodeZapException e) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다. 다시 로그인해주세요.");
        }
    }
}
