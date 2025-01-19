package codezap.auth.manager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import codezap.auth.dto.Credential;

/**
 * Credential 정보를 Http 응답에 설정하기 위한 클래스입니다.
 */
public interface CredentialManager {

    boolean support(CredentialType credentialType);

    Credential getCredential(HttpServletRequest httpServletRequest);

    boolean hasCredential(HttpServletRequest httpServletRequest);

    void setCredential(HttpServletResponse httpServletResponse, Credential credential);

    void removeCredential(HttpServletResponse httpServletResponse);
}
