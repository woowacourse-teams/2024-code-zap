package codezap.auth.manager;

import codezap.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CredentialManager {

    String getCredential(HttpServletRequest httpServletRequest);

    Member getMember(HttpServletRequest httpServletRequest);

    boolean hasCredential(HttpServletRequest httpServletRequest);

    void setCredential(HttpServletResponse httpServletResponse, String credential);

    void removeCredential(HttpServletResponse httpServletResponse);
}
