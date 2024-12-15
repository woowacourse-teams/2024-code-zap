package codezap.auth.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codezap.auth.dto.Credential;
import codezap.auth.dto.LoginMember;
import codezap.auth.provider.CredentialProvider;
import codezap.auth.provider.PlainCredentialProvider;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

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
        authorizationHeaderCredentialManager = new AuthorizationHeaderCredentialManager();
    }

    @Nested
    @DisplayName("요청에서 회원 반환")
    class GetMember {

        @Test
        @DisplayName("회원 반환 성공")
        void getCredential_WithValidCookie_ReturnsCredential() {
            //given
            LoginMember loginMember = LoginMember.from(MemberFixture.getFirstMember());
            Credential credential = credentialProvider.createCredential(loginMember);
            request.addHeader(HttpHeaders.AUTHORIZATION, credential.toAuthorizationHeader());

            //when & then
            Credential extractedCredential = authorizationHeaderCredentialManager.getCredential(request);
            Member member = credentialProvider.extractMember(extractedCredential);
            assertAll(
                    () -> assertThat(member.getId()).isEqualTo(loginMember.id()),
                    () -> assertThat(member.getName()).isEqualTo(loginMember.name()),
                    () -> assertThat(member.getPassword()).isEqualTo(loginMember.password()),
                    () -> assertThat(member.getSalt()).isEqualTo(loginMember.salt())
            );
        }

        @Test
        @DisplayName("회원 반환 실패: 헤더 없음")
        void getCredential_WithNoCookies_ThrowsException() {
            assertThatThrownBy(() -> authorizationHeaderCredentialManager.getCredential(request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("헤더가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        }
    }

    @Test
    @DisplayName("인증 정보 헤더에 추가 성공")
    void setCredential_SetsCredentialCookie() {
        //given
        Member member = MemberFixture.getFirstMember();
        Credential credential = credentialProvider.createCredential(LoginMember.from(member));

        //when
        authorizationHeaderCredentialManager.setCredential(response, credential);

        //then
        String header = response.getHeader(HttpHeaders.AUTHORIZATION);
        assertThat(header).isEqualTo(credential.toAuthorizationHeader());
    }
}
