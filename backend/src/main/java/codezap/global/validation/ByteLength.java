package codezap.global.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ByteLengthValidator.class)
public @interface ByteLength {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int max();

    int min() default 0;
}
