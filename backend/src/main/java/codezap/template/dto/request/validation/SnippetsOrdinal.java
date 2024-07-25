package codezap.template.dto.request.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SnippetsOrdinalValidator.class)
public @interface SnippetsOrdinal {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
