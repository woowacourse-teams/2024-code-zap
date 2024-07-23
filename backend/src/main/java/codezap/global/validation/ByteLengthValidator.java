package codezap.global.validation;

import java.nio.charset.StandardCharsets;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ByteLengthValidator implements ConstraintValidator<ByteLength, String> {

    private int max;
    private int min;

    @Override
    public void initialize(ByteLength constraintAnnotation) {
        max = constraintAnnotation.max();
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String target, ConstraintValidatorContext constraintValidatorContext) {
        int byteLength = target.getBytes(StandardCharsets.UTF_8).length;
        return min <= byteLength && byteLength <= max;
    }
}
