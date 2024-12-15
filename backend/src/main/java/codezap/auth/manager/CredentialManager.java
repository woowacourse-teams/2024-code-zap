package codezap.auth.manager;

import codezap.auth.dto.Credential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CredentialManager {

    Credential getCredential(HttpServletRequest httpServletRequest);

    boolean hasCredential(HttpServletRequest httpServletRequest);

    void setCredential(HttpServletResponse httpServletResponse, Credential credential);

    void removeCredential(HttpServletResponse httpServletResponse);
}
