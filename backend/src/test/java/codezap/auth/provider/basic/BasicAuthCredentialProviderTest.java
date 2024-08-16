package codezap.auth.provider.basic;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;

import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;

class BasicAuthCredentialProviderTest {

    private final MemberRepository memberRepository = new FakeMemberRepository();

    private BasicAuthCredentialProvider basicAuthCredentialProvider;

    @BeforeEach
    void setUp() {
        basicAuthCredentialProvider = new BasicAuthCredentialProvider(memberRepository);
    }

    @Test
    @DisplayName("BasicAuth 인증 정보 생성 성공")
    void createCredential() {
        Member member = MemberFixture.memberFixture();
        String actualCredential = basicAuthCredentialProvider.createCredential(member);

        String expectedCredential = HttpHeaders.encodeBasicAuth(member.getLoginId(), member.getPassword(),
                StandardCharsets.UTF_8);
        assertEquals(actualCredential, expectedCredential);
    }

    @Nested
    @DisplayName("인증 정보로부터 회원 추출")
    class extractMember {
        @Test
        @DisplayName("회원 추출 성공")
        void extractMember() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            String credential = basicAuthCredentialProvider.createCredential(member);

            assertEquals(member, basicAuthCredentialProvider.extractMember(credential));
        }

        @Test
        @DisplayName("회원 추출 실패: 존재하지 않는 회원")
        void extractMemberThrowNotExistMember() {
            Member unsaverdMember = MemberFixture.memberFixture();
            String credential = basicAuthCredentialProvider.createCredential(unsaverdMember);

            assertThatThrownBy(() -> basicAuthCredentialProvider.extractMember(credential))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("존재하지 않는 아이디 " + unsaverdMember.getLoginId() + " 입니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"wrongPassword", " "})
        @DisplayName("회원 추출 실패: 잘못된 비밀번호로 생성된 인증 값")
        void extractMemberThrow(String wrongPassword) {
            Member savedMember = memberRepository.save(MemberFixture.memberFixture());
            String wrongCredential = HttpHeaders.encodeBasicAuth(savedMember.getLoginId(), wrongPassword,
                    StandardCharsets.UTF_8);

            assertThatThrownBy(() -> basicAuthCredentialProvider.extractMember(wrongCredential))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }
}
