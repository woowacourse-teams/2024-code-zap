package codezap.auth.manager;

import codezap.auth.dto.Credential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Credential 정보를 Http 응답에 설정하기 위한 클래스입니다.
 */
public interface CredentialManager {

    Credential getCredential(HttpServletRequest httpServletRequest);

    boolean hasCredential(HttpServletRequest httpServletRequest);

    void setCredential(HttpServletResponse httpServletResponse, Credential credential);

    void removeCredential(HttpServletResponse httpServletResponse);
}
