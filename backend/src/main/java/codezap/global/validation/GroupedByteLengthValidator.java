package codezap.global.validation;

import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GroupedByteLengthValidator implements ConstraintValidator<ByteLength, List<String>> {

    private int max;
    private int min;

    @Override
    public void initialize(ByteLength constraintAnnotation) {
        max = constraintAnnotation.max();
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(List<String> strings, ConstraintValidatorContext constraintValidatorContext) {
        return strings.stream()
                .allMatch(this::isValid);
    }

    private boolean isValid(String target) {
        int byteLength = target.getBytes(StandardCharsets.UTF_8).length;
        return min <= byteLength && byteLength <= max;
    }
}
