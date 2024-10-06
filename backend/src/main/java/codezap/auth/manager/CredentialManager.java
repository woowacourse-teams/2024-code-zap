package codezap.auth.manager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CredentialManager {

    String getCredential(HttpServletRequest httpServletRequest);

    boolean hasCredential(final HttpServletRequest httpServletRequest);

    void setCredential(HttpServletResponse httpServletResponse, String credential);

    void removeCredential(HttpServletResponse httpServletResponse);
}
