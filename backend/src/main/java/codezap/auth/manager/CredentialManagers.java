package codezap.auth.manager;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CredentialManagers {

    private static final String CREDENTIAL_TYPE_HEADER = "Credential-Type";
    private final List<CredentialManager> credentialManagers;

    public CredentialManager credentialManager(HttpServletRequest request) {
        CredentialType credentialType = CredentialType.valueOf(request.getHeader(CREDENTIAL_TYPE_HEADER));
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


}
