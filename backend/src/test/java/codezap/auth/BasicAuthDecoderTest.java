package codezap.auth;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;

import codezap.global.exception.CodeZapException;

class BasicAuthDecoderTest {

    @Test
    @DisplayName("BasicAuth 인증 정보 디코딩 성공")
    void decodeValidBasicAuth() {
        String email = "user@example.com";
        String password = "password123";
        String credential = HttpHeaders.encodeBasicAuth(email, password, StandardCharsets.UTF_8);

        String[] result = BasicAuthDecoder.decodeBasicAuth(credential);

        assertAll(
                () -> assertEquals(2, result.length),
                () -> assertEquals(email, result[0]),
                () -> assertEquals(password, result[1])
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid_base64!", "=", " "})
    @DisplayName("BasicAuth 인증 정보 디코딩 실패: BasicAuth 형식 오류")
    void throwExceptionForInvalidBase64(String invalidAuth) {
        assertThatThrownBy(() -> BasicAuthDecoder.decodeBasicAuth(invalidAuth))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("잘못된 Base64 인코딩입니다.");
    }


    @Test
    @DisplayName("BasicAuth 인증 정보 디코딩 실패: 구분자 누락")
    void throwExceptionForMissingSeparator() {
        String invalidCredential = Base64.getEncoder()
                .encodeToString("userexample.com" .getBytes(StandardCharsets.UTF_8));

        assertThatCode(() -> BasicAuthDecoder.decodeBasicAuth(invalidCredential))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("인증 정보가 올바르지 않습니다. 다시 로그인 해주세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {":", "user:", ":password"})
    @DisplayName("BasicAuth 인증 정보 디코딩 실패: 빈 사용자 이름 또는 비밀번호")
    void throwExceptionForEmptyUsernameOrPassword(String auth) {
        String invalidCredential = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> BasicAuthDecoder.decodeBasicAuth(invalidCredential))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("인증 정보가 올바르지 않습니다. 다시 로그인 해주세요.");
    }

    @Test
    @DisplayName("BasicAuth 인증 정보 디코딩 성공: 여러 개의 구분자 포함일 경우 첫 구분자 이후 문자열을 비밀번호로 인식")
    void decodeValidAuthWithMultipleSeparators() {
        String email = "user@example.com";
        String password = "pass:word:123";
        String credential = HttpHeaders.encodeBasicAuth(email, password, StandardCharsets.UTF_8);

        String[] result = BasicAuthDecoder.decodeBasicAuth(credential);

        assertThat(result).hasSize(2)
                .containsExactly(email, password);
    }
}
