package codezap.auth.manager;

import codezap.auth.provider.CredentialProvider;
import codezap.member.domain.Member;
import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class CookieCredentialManager implements CredentialManager {

    private static final String CREDENTIAL_COOKIE_NAME = "credential";

    private final CredentialProvider credentialProvider;

    @Override
    public Member getMember(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        checkCookieExist(cookies);

        Cookie credentialCookie = extractTokenCookie(cookies);
        String credential = credentialCookie.getValue();
        return credentialProvider.extractMember(credential);
    }

    private void checkCookieExist(final Cookie[] cookies) {
        if (cookies == null) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
    }

    @Override
    public boolean hasCredential(final HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return false;
        }
        return Arrays.stream(cookies)
                .anyMatch(cookie -> cookie.getName().equals(CREDENTIAL_COOKIE_NAME));
    }

    private Cookie extractTokenCookie(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(CREDENTIAL_COOKIE_NAME))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(ErrorCode.UNAUTHORIZED_USER,
                        "인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."));
    }

    @Override
    public void setCredential(HttpServletResponse httpServletResponse, Member member) {
        String token = credentialProvider.createCredential(member);
        ResponseCookie responseCookie = ResponseCookie.from(CREDENTIAL_COOKIE_NAME, token)
                .maxAge(-1)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    @Override
    public void removeCredential(final HttpServletResponse httpServletResponse) {
        ResponseCookie responseCookie = ResponseCookie.from(CREDENTIAL_COOKIE_NAME)
                .maxAge(0)
                .build();
        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}
