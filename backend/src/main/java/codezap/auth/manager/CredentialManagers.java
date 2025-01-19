package codezap.auth.manager;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import codezap.auth.dto.Credential;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CredentialManagers {

    protected static final String CREDENTIAL_TYPE_HEADER = "Credential-Type";
    private final List<CredentialManager> credentialManagers;

    public Credential getCredential(HttpServletRequest request) {
        if (!hasCredential(request)) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증 정보가 없습니다. 다시 로그인해 주세요.");
        }
        return getCredentialManager(request).getCredential(request);
    }

    public boolean hasCredential(HttpServletRequest request) {
        return getCredentialManager(request).hasCredential(request);
    }

    public void setCredential(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Credential credential
    ) {
        getCredentialManager(httpServletRequest).setCredential(httpServletResponse, credential);
    }

    private CredentialManager getCredentialManager(HttpServletRequest request) {
        Optional<CredentialType> credentialType = getCredentialType(request);
        if (credentialType.isEmpty()) {
            return getDefaultCredentialManager();
        }
        return credentialManagers.stream()
                .filter(cm -> cm.support(credentialType.get()))
                .findFirst()
                .orElse(getDefaultCredentialManager());
    }

    private Optional<CredentialType> getCredentialType(HttpServletRequest request) {
        String credentialTypeHeaderValue = request.getHeader(CREDENTIAL_TYPE_HEADER);
        if (credentialTypeHeaderValue == null) {
            return Optional.empty();
        }
        return Optional.of(CredentialType.findByHeaderValue(credentialTypeHeaderValue));
    }

    private CredentialManager getDefaultCredentialManager() {
        return credentialManagers.stream()
                .filter(cm -> cm instanceof CookieCredentialManager)
                .findFirst()
                .get();
    }

    public void removeCredential(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        getCredentialManager(httpServletRequest).removeCredential(httpServletResponse);
    }
}
