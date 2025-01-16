package codezap.auth.manager;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import codezap.auth.dto.Credential;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CredentialManagers {

    protected static final String CREDENTIAL_TYPE_HEADER = "Credential-Type";
    private final List<CredentialManager> credentialManagers;

    public void setCredential(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Credential credential) {
        getCredentialManager(httpServletRequest).setCredential(httpServletResponse, credential);
    }

    private CredentialManager getCredentialManager(HttpServletRequest request) {
        CredentialType credentialType = getCredentialType(request);
        return credentialManagers.stream()
                .filter(cm -> cm.support(credentialType))
                .findFirst()
                .orElse(getDefaultCredentialManager());
    }

    private CredentialType getCredentialType(HttpServletRequest request) {
        return CredentialType.findByHeaderValue(request.getHeader(CREDENTIAL_TYPE_HEADER));
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
