package codezap.auth.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import codezap.auth.dto.Credential;
import codezap.global.exception.CodeZapException;

class CredentialManagersTest {

    private final CredentialManagers credentialManagers = new CredentialManagers(List.of(
            new AuthorizationHeaderCredentialManager(),
            new CookieCredentialManager()
    ));

    @Nested
    @DisplayName("Credential 설정")
    class SetCredential {
        Credential credential = Credential.basic("credential");

        @Test
        @DisplayName("성공: 쿠키를 사용한다고 명시한 경우 쿠키에 Credential 을 설정한다.")
        void usingCookie() {
            //given
            var httpServletRequest = new MockHttpServletRequest();
            var httpServletResponse = new MockHttpServletResponse();
            httpServletRequest.addHeader(CredentialManagers.CREDENTIAL_TYPE_HEADER,
                    CredentialType.COOKIE.getHeaderValue());

            //when
            credentialManagers.setCredential(httpServletRequest, httpServletResponse, credential);

            //then
            assertThat(httpServletResponse.getHeader("Set-Cookie"))
                    .contains("credential");
        }

        @Test
        @DisplayName("성공: Authorization 헤더를 사용한다고 명시한 경우 Authorization 헤더에 Credential 을 설정한다.")
        void usingCredentialHeader() {
            //given
            var httpServletRequest = new MockHttpServletRequest();
            var httpServletResponse = new MockHttpServletResponse();
            httpServletRequest.addHeader(CredentialManagers.CREDENTIAL_TYPE_HEADER,
                    CredentialType.AUTHORIZATION_HEADER.getHeaderValue());

            //when
            credentialManagers.setCredential(httpServletRequest, httpServletResponse, credential);

            //then
            assertThat(httpServletResponse.getHeader("Authorization"))
                    .contains("credential");
        }

        @Test
        @DisplayName("성공: 아무것도 명시하지 않은 경우 쿠키에 Credential 을 설정한다.")
        void defaultCredential() {
            //given
            var httpServletRequest = new MockHttpServletRequest();
            var httpServletResponse = new MockHttpServletResponse();
            httpServletRequest.addHeader(CredentialManagers.CREDENTIAL_TYPE_HEADER,
                    CredentialType.COOKIE.getHeaderValue());

            //when
            credentialManagers.setCredential(httpServletRequest, httpServletResponse, credential);

            //then
            assertThat(httpServletResponse.getHeader("Set-Cookie"))
                    .contains("credential");
        }

        @Test
        @DisplayName("실패: 잘못된 지원 타입이 명시될 경우 예외가 발생한다.")
        void unsupportedCredentialType() {
            //given
            var request = new MockHttpServletRequest();
            var response = new MockHttpServletResponse();
            request.addHeader(CredentialManagers.CREDENTIAL_TYPE_HEADER, "Unsupported Credential Type");

            //when & then
            assertThatThrownBy(() -> credentialManagers.setCredential(request, response, credential))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("지원하지 않는 인증 타입입니다.");
        }
    }

    @Nested
    @DisplayName("Credential 삭제")
    class RemoveCredential {

        @Test
        @DisplayName("성공: 쿠키를 사용한다고 명시한 경우 쿠키를 무효화한다.")
        void expireCookie() {
            //given
            var httpServletRequest = new MockHttpServletRequest();
            var httpServletResponse = new MockHttpServletResponse();
            httpServletRequest.addHeader(CredentialManagers.CREDENTIAL_TYPE_HEADER,
                    CredentialType.COOKIE.getHeaderValue());

            //when
            credentialManagers.removeCredential(httpServletResponse);

            //then
            assertThat(httpServletResponse.getHeader("Set-Cookie"))
                    .contains("Max-Age=0");
        }

        @Test
        @DisplayName("성공: Authorization 헤더를 사용한다고 명시한 경우 서버에서 처리를 하지 않는다.")
        void usingCredentialHeader() {
        }

        @Test
        @DisplayName("성공: 아무것도 명시하지 않은 경우 쿠키를 무효화한다.")
        void defaultCredential() {
            //given
            var httpServletRequest = new MockHttpServletRequest();
            var httpServletResponse = new MockHttpServletResponse();
            httpServletRequest.addHeader(CredentialManagers.CREDENTIAL_TYPE_HEADER,
                    CredentialType.COOKIE.getHeaderValue());

            //when
            credentialManagers.removeCredential(httpServletResponse);

            //then
            assertThat(httpServletResponse.getHeader("Set-Cookie"))
                    .contains("Max-Age=0");
        }
    }
}
