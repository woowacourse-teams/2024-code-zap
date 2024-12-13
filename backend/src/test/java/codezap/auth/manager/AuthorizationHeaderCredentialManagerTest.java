package codezap.auth.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import codezap.auth.provider.CredentialProvider;
import codezap.auth.provider.PlainCredentialProvider;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;

class AuthorizationHeaderCredentialManagerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private CredentialProvider credentialProvider;
    private AuthorizationHeaderCredentialManager authorizationHeaderCredentialManager;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        credentialProvider = new PlainCredentialProvider();
        authorizationHeaderCredentialManager = new AuthorizationHeaderCredentialManager(credentialProvider);
    }

    @Nested
    @DisplayName("요청에서 회원 반환")
    class GetMember {

        @Test
        @DisplayName("회원 반환 성공")
        void getCredential_WithValidCookie_ReturnsCredential() {
            Member member = MemberFixture.getFirstMember();
            Credential credential = credentialProvider.createCredential(member);
            request.addHeader(HttpHeaders.AUTHORIZATION, credential.toAuthorizationHeader());

            assertEquals(authorizationHeaderCredentialManager.getMember(request), member);
        }

        @Test
        @DisplayName("회원 반환 실패: 헤더 없음")
        void getCredential_WithNoCookies_ThrowsException() {
            assertThatThrownBy(() -> authorizationHeaderCredentialManager.getMember(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("헤더가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
    }

    @Test
    @DisplayName("인증 정보 헤더에 추가 성공")
    void setCredential_SetsCredentialCookie() {
        Member member = MemberFixture.getFirstMember();
        Credential credential = credentialProvider.createCredential(MemberFixture.getFirstMember());

        authorizationHeaderCredentialManager.setCredential(response, member);

        String header = response.getHeader(HttpHeaders.AUTHORIZATION);
        assertThat(header).isEqualTo(credential.toAuthorizationHeader());
    }
}
