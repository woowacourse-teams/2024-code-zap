package codezap.auth.manager.header;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationHeaderCredentialManager implements CredentialManager {

    private final CredentialProvider credentialProvider;

    @Override
    public Member getMember(HttpServletRequest httpServletRequest) {
        String authorizationHeaderValue = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        checkHeaderExist(authorizationHeaderValue);

        HeaderCredential headerCredential = HeaderCredential.from(authorizationHeaderValue);
        checkAuthorizationType(headerCredential.type());
        return credentialProvider.extractMember(headerCredential.value());
    }

    private void checkHeaderExist(String authorizationHeaderValue) {
        if (authorizationHeaderValue == null) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "헤더가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
    }

    private void checkAuthorizationType(String authorizationType) {
        String expectedType = credentialProvider.getType();
        if (!expectedType.equals(authorizationType)) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_USER, credentialProvider.getType() + " 타입의 인증 정보가 아닙니다.");
        }
    }

    @Override
    public boolean hasCredential(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) != null;
    }

    @Override
    public void setCredential(HttpServletResponse httpServletResponse, Member member) {
        HeaderCredential headerCredential = new HeaderCredential(credentialProvider.getType(), credentialProvider.createCredential(member));
        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, headerCredential.toAuthorizationHeader());
    }

    @Override
    public void removeCredential(HttpServletResponse httpServletResponse) {
    }
}
