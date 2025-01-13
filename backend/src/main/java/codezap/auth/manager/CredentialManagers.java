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
        CredentialType credentialType = CredentialType.findByHeaderValue(request.getHeader(CREDENTIAL_TYPE_HEADER));
        return credentialManagers.stream()
                .filter(cm -> cm.support(credentialType))
                .findFirst()
                .orElse(getDefaultCredentialManager());
    }

    private CredentialManager getDefaultCredentialManager() {
        return credentialManagers.stream()
                .filter(cm -> cm instanceof CookieCredentialManager)
                .findFirst()
                .get();
    }

    public void removeCredential(HttpServletResponse httpServletResponse) {
        credentialManagers.forEach(cm -> cm.removeCredential(httpServletResponse));
    }
}
