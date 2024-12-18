package codezap.auth.manager;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codezap.auth.dto.Credential;
import codezap.global.exception.CodeZapException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CredentialTest {

    @Nested
    @DisplayName("헤더 값으로 객체 생성")
    class From {

        @Test
        @DisplayName("객체 생성 성공")
        void success() {
            assertThatCode(() -> Credential.from("type credential"))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {"111", "type credential additional-value"})
        @DisplayName("객체 생성 실패: 잘못된 헤더 값으로 객체 생성 실패")
        void generateFromWrongHeader(String wrongHeaderValue) {
            assertThatThrownBy(() -> Credential.from(wrongHeaderValue))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("인증 헤더의 값이 잘못되었습니다.");
        }
    }
}
