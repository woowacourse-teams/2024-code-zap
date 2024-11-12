package codezap.voc.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Stream;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class VocRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void success() {
        var content = "lorem ipsum dolor sit amet consectetur adipiscing elit fugiat cupiditat";
        var email = "codezap2024@gmail.com";
        var request = new VocRequest(content, email);

        Set<ConstraintViolation<VocRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations).isEmpty();
    }

    @Nested
    @DisplayName("이메일 검증")
    class Email {

        @Test
        @DisplayName("이메일이 null인 경우에도 정상 동작")
        void email_null_success() {
            var content = "lorem ipsum dolor sit amet consectetur adipiscing elit fugiat cupiditat";
            var request = new VocRequest(content, null);

            Set<ConstraintViolation<VocRequest>> constraintViolations = validator.validate(request);

            assertThat(constraintViolations).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "codezap", "@gmail.com", ".com"})
        @DisplayName("이메일 형식에 맞지 않는 경우 예외 발생")
        void email_format_fail(String email) {
            var content = "lorem ipsum dolor sit amet consectetur adipiscing elit fugiat cupiditat";
            var request = new VocRequest(content, email);

            Set<ConstraintViolation<VocRequest>> constraintViolations = validator.validate(request);

            assertThat(constraintViolations).hasSize(1)
                    .first()
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("올바른 형식의 이메일 주소여야 합니다.");
        }
    }


    @Nested
    @DisplayName("문의 내용 검증")
    class Content {

        @ParameterizedTest
        @MethodSource
        @DisplayName("문의 내용 길이 임계값 검증 성공: 최솟값 20자, 최댓값 10,000자")
        void content_length_success(String content) {
            var request = new VocRequest(content, null);

            Set<ConstraintViolation<VocRequest>> constraintViolations = validator.validate(request);

            assertThat(constraintViolations).isEmpty();
        }

        static Stream<String> content_length_success() {
            var contentLength20 = "z".repeat(20);
            var contentLength10_000 = "z".repeat(10_000);
            return Stream.of(contentLength20, contentLength10_000);
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("문의 내용이 20자 미만, 10,000자 초과일 경우 예외 발생")
        void content_length_fail(String content) {
            // given
            var request = new VocRequest(content, null);

            // when
            Set<ConstraintViolation<VocRequest>> constraintViolations = validator.validate(request);

            // then
            assertThat(constraintViolations).hasSize(1)
                    .first()
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("문의 내용은 최소 20자, 최대 10,000 자 입력할 수 있습니다.");
        }

        static Stream<String> content_length_fail() {
            var contentLengthUnder20 = "code zap";
            var contentLengthOver10_000 = "z".repeat(10_001);
            return Stream.of(contentLengthUnder20, contentLengthOver10_000);
        }

        @Test
        @DisplayName("문의 내용이 null인 경우 예외 발생")
        void content_null_fail() {
            var request = new VocRequest(null, null);

            Set<ConstraintViolation<VocRequest>> constraintViolations = validator.validate(request);

            assertThat(constraintViolations).hasSize(1)
                    .first()
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("문의 내용은 비어있을 수 없습니다.");
        }
    }

}
