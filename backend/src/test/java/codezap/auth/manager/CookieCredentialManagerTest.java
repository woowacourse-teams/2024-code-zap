package codezap.auth.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codezap.auth.dto.LoginMember;
import codezap.auth.provider.CredentialProvider;
import codezap.auth.provider.PlainCredentialProvider;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import jakarta.servlet.http.Cookie;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class CookieCredentialManagerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private CookieCredentialManager cookieCredentialManager;
    private CredentialProvider credentialProvider;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        credentialProvider = new PlainCredentialProvider();
        cookieCredentialManager = new CookieCredentialManager(credentialProvider);
    }


    @Nested
    @DisplayName("요청에서 회원 반환")
    class GetMember {

        @Test
        @DisplayName("회원 반환 성공")
        void getCredential_WithValidCookie_ReturnsCredential() {
            //given
            Member member = MemberFixture.getFirstMember();
            Credential credential = credentialProvider.createCredential(LoginMember.from(member));
            request.setCookies(new Cookie("credential", credential.value()));

            //when & then
            assertThat(cookieCredentialManager.getMember(request))
                    .isEqualTo(member);
        }

        @Test
        @DisplayName("회원 반환 실패: 쿠키 없음")
        void getCredential_WithNoCookies_ThrowsException() {
            assertThatThrownBy(() -> cookieCredentialManager.getMember(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }

        @Test
        @DisplayName("회원 반환 실패: 인증 정보에 대한 쿠키 없음")
        void getCredential_WithNoCredentialCookie_ThrowsException() {
            request.setCookies(new Cookie("other-cookie", "some-value"));

            assertThatThrownBy(() -> cookieCredentialManager.getMember(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
    }

    @Test
    @DisplayName("인증 정보 쿠키에 추가 성공")
    void setCredential_SetsCredentialCookie() {
        //given
        Member member = MemberFixture.getFirstMember();

        //when
        cookieCredentialManager.setCredential(response, LoginMember.from(member));

        //then
        Cookie cookie = response.getCookie("credential");
        assertAll(
                () -> assertThat(cookie).isNotNull(),
                () -> assertThat(Objects.requireNonNull(cookie).getValue()).isEqualTo(
                        credentialProvider.createCredential(LoginMember.from(member)).value()),
                () -> assertThat(Objects.requireNonNull(cookie).getMaxAge()).isEqualTo(-1),
                () -> assertThat(Objects.requireNonNull(cookie).getPath()).isEqualTo("/"),
                () -> assertThat(Objects.requireNonNull(cookie).isHttpOnly()).isTrue(),
                () -> assertThat(Objects.requireNonNull(cookie).getSecure()).isTrue()
        );
    }

    @Test
    @DisplayName("인증 정보 쿠키에서 제거 성공")
    void removeCredential_RemovesCredentialCookie() {
        cookieCredentialManager.setCredential(response, LoginMember.from(MemberFixture.getFirstMember()));

        cookieCredentialManager.removeCredential(response);

        assertThatThrownBy(() -> cookieCredentialManager.getMember(request))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
    }
}
