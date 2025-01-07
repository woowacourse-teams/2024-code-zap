package codezap.global.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrdinalValidator.class)
public @interface Ordinal {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
