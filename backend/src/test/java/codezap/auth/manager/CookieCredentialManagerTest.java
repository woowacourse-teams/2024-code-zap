package codezap.auth.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import codezap.global.exception.CodeZapException;

class CookieCredentialManagerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private CookieCredentialManager cookieCredentialManager;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        cookieCredentialManager = new CookieCredentialManager();
    }

    @Nested
    @DisplayName("인증 정보 반환")
    class getCredential {

        @Test
        @DisplayName("인증 정보 반환 성공")
        void getCredential_WithValidCookie_ReturnsCredential() {
            String credential = "test-token";
            request.setCookies(new Cookie("credential", credential));

            assertEquals(cookieCredentialManager.getCredential(request), credential);
        }

        @Test
        @DisplayName("인증 정보 반환 실패: 쿠키 없음")
        void getCredential_WithNoCookies_ThrowsException() {
            assertThatThrownBy(() -> cookieCredentialManager.getCredential(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }

        @Test
        @DisplayName("인증 정보 반환 실패: 인증 정보에 대한 쿠키 없음")
        void getCredential_WithNoCredentialCookie_ThrowsException() {
            request.setCookies(new Cookie("other-cookie", "some-value"));

            assertThatThrownBy(() -> cookieCredentialManager.getCredential(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
    }

    @Test
    @DisplayName("인증 정보 쿠키에 추가 성공")
    void setCredential_SetsCredentialCookie() {
        String token = "test-token";

        cookieCredentialManager.setCredential(response, token);

        Cookie cookie = response.getCookie("credential");
        assertAll(
                () -> assertThat(cookie).isNotNull(),
                () -> assertThat(Objects.requireNonNull(cookie).getValue()).isEqualTo(token),
                () -> assertThat(Objects.requireNonNull(cookie).getMaxAge()).isEqualTo(-1),
                () -> assertThat(Objects.requireNonNull(cookie).getPath()).isEqualTo("/"),
                () -> assertThat(Objects.requireNonNull(cookie).isHttpOnly()).isTrue(),
                () -> assertThat(Objects.requireNonNull(cookie).getSecure()).isTrue()
        );
    }

    @Test
    @DisplayName("인증 정보 쿠키에서 제거 성공")
    void removeCredential_RemovesCredentialCookie() {
        cookieCredentialManager.setCredential(response, "test-token");

        cookieCredentialManager.removeCredential(response);

        assertThatThrownBy(() -> cookieCredentialManager.getCredential(request))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
    }
}
