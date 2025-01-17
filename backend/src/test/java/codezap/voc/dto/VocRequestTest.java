package codezap.voc.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class VocRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private static String message = "lorem ipsum";
    private static String email = "codezap2024@gmail.com";
    private static Long memberId = 1L;
    private static String name = "만두";

    private VocRequest sut;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("성공: email, memberId, name은 optional")
    void success(String message, String email, Long memberId, String name) {
        sut = new VocRequest(message, email, memberId, name);

        var constraintViolations = validator.validate(sut);

        assertThat(constraintViolations).isEmpty();
    }

    static Stream<Arguments> success() {
        return Stream.of(
                Arguments.of(message, email, memberId, name),
                Arguments.of(message, email, memberId, null),
                Arguments.of(message, email, null, null),
                Arguments.of(message, null, null, null));
    }

    @Nested
    @DisplayName("문의 내용 검증")
    class MessageTest {

        @ParameterizedTest
        @MethodSource
        @DisplayName("성공: 문의 내용 길이 10글자부터 10,000글자")
        void message_length_success(String message) {
            sut = new VocRequest(message);

            var constraintViolations = validator.validate(sut);

            assertThat(constraintViolations).isEmpty();
        }

        static Stream<String> message_length_success() {
            var messageLength20 = RandomStringUtils.randomAlphanumeric(10);
            var messageLength10_000 = RandomStringUtils.randomAlphanumeric(10_000);
            return Stream.of(messageLength20, messageLength10_000);
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("실패: 문의 내용 길이 9자 이하, 10,001글자 이상")
        void message_length_fail(String message) {
            sut = new VocRequest(message);

            var constraintViolations = validator.validate(sut);

            assertThat(constraintViolations).isNotEmpty()
                    .first()
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("문의 내용은 최소 10자, 최대 10,000 자 입력할 수 있습니다.");
        }

        static Stream<String> message_length_fail() {
            var messageLength19 = RandomStringUtils.randomAlphanumeric(9);
            var messageLength10_001 = RandomStringUtils.randomAlphanumeric(10_001);
            return Stream.of(messageLength19, messageLength10_001);
        }

        @Test
        @DisplayName("실패: 문의 내용이 null인 경우")
        void message_null_fail() {
            sut = new VocRequest(null);

            var constraintViolations = validator.validate(sut);

            assertThat(constraintViolations).isNotEmpty()
                    .first()
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("문의 내용은 비어있을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("이메일 검증")
    class EmailTest {

        @ParameterizedTest
        @ValueSource(strings = {"", "codezap", "@gmail.com", ".com"})
        @DisplayName("실패: 이메일 형식에 맞지 않는 경우")
        void email_format_fail(String email) {
            sut = new VocRequest(message, email);

            var constraintViolations = validator.validate(sut);

            assertThat(constraintViolations).isNotEmpty()
                    .first()
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("올바른 형식의 이메일 주소여야 합니다.");
        }
    }

    @Nested
    @DisplayName("memberId 검증")
    class MemberIdTest {

        @Test
        @DisplayName("실패: memberId가 0인 경우 예외 발생")
        void memberId_fail() {
            memberId = 0L;
            sut = new VocRequest(message, email, memberId, name);

            var constraintViolations = validator.validate(sut);

            assertThat(constraintViolations).isNotEmpty()
                    .first()
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("1 이상이어야 합니다.");
        }
    }

    @Nested
    @DisplayName("로그인한 사용자 이름 검증")
    class NameTest {

        @Test
        @DisplayName("성공: 사용자 이름 길이 255글자")
        void name_success() {
            name = RandomStringUtils.randomAlphanumeric(255);
            sut = new VocRequest(message, email, memberId, name);

            var constraintViolations = validator.validate(sut);

            assertThat(constraintViolations).isEmpty();
        }

        @Test
        @DisplayName("실패: 사용자 이름 길이 256글자부터 예외 발생")
        void name_fail() {
            name = RandomStringUtils.randomAlphanumeric(256);
            sut = new VocRequest(message, email, memberId, name);

            var constraintViolations = validator.validate(sut);

            assertThat(constraintViolations).isNotEmpty()
                    .first()
                    .extracting(ConstraintViolation::getMessage)
                    .isEqualTo("아이디는 255자 이하로 입력해주세요.");
        }
    }
}
