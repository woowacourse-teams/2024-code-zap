package codezap.auth.provider.basic;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import codezap.auth.manager.Credential;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.MemberRepository;

class BasicAuthCredentialProviderTest {

    private MemberRepository memberRepository;
    private BasicAuthCredentialProvider basicAuthCredentialProvider;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        basicAuthCredentialProvider = new BasicAuthCredentialProvider(memberRepository);
    }

    @Test
    @DisplayName("BasicAuth 인증 정보 생성 성공")
    void createCredential() {
        Member member = MemberFixture.memberFixture();
        Credential actualCredential = basicAuthCredentialProvider.createCredential(member);

        Credential expectedCredential = Credential.basic(HttpHeaders.encodeBasicAuth(member.getName(), member.getPassword(), StandardCharsets.UTF_8));
        assertEquals(actualCredential, expectedCredential);
    }

    @Nested
    @DisplayName("인증 정보로부터 회원 추출")
    class ExtractMember {

        @Test
        @DisplayName("회원 추출 성공")
        void extractMember() {
            Member member = MemberFixture.memberFixture();

            when(memberRepository.fetchByName(any())).thenReturn(member);

            Credential credential = basicAuthCredentialProvider.createCredential(member);
            assertEquals(member, basicAuthCredentialProvider.extractMember(credential));
        }

        @Test
        @DisplayName("회원 추출 실패: 존재하지 않는 회원")
        void extractMemberThrowNotExistMember() {
            Member unsaverdMember = MemberFixture.memberFixture();
            Credential credential = basicAuthCredentialProvider.createCredential(unsaverdMember);

            doThrow(new CodeZapException(ErrorCode.INVALID_REQUEST, "존재하지 않는 아이디 " + unsaverdMember.getName() + " 입니다."))
                    .when(memberRepository).fetchByName(any());

            assertThatThrownBy(() -> basicAuthCredentialProvider.extractMember(credential))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("존재하지 않는 아이디 " + unsaverdMember.getName() + " 입니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"wrongPassword", " "})
        @DisplayName("회원 추출 실패: 잘못된 비밀번호로 생성된 인증 값")
        void extractMemberThrow(String wrongPassword) {
            Member savedMember = MemberFixture.memberFixture();
            String wrongCredentialValue = HttpHeaders.encodeBasicAuth(savedMember.getName(), wrongPassword, StandardCharsets.UTF_8);
            Credential wrongCredential = Credential.basic(wrongCredentialValue);

            when(memberRepository.fetchByName(any())).thenReturn(savedMember);

            assertThatThrownBy(() -> basicAuthCredentialProvider.extractMember(wrongCredential))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("비밀번호가 일치하지 않습니다.");
        }
    }
}
