package codezap.auth.manager;

import codezap.auth.dto.Credential;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
public class AuthorizationHeaderCredentialManager implements CredentialManager {

    @Override
    public Credential getCredential(HttpServletRequest httpServletRequest) {
        String authorizationHeaderValue = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        checkHeaderExist(authorizationHeaderValue);

        return Credential.from(authorizationHeaderValue);
    }

    private void checkHeaderExist(String authorizationHeaderValue) {
        if (authorizationHeaderValue == null) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "헤더가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
    }

    @Override
    public boolean hasCredential(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) != null;
    }

    @Override
    public void setCredential(HttpServletResponse httpServletResponse, Credential credential) {
        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, credential.toAuthorizationHeader());
    }

    @Override
    public void removeCredential(HttpServletResponse httpServletResponse) {
    }
}
