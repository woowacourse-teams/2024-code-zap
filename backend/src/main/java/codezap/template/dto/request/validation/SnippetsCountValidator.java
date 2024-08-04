package codezap.template.dto.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SnippetsCountValidator implements ConstraintValidator<SnippetsCount, ValidatedSnippetsCountRequest> {

    @Override
    public boolean isValid(ValidatedSnippetsCountRequest validatedSnippetsCountRequest,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        Integer snippetsCount = validatedSnippetsCountRequest.countSnippets();
        return snippetsCount > 0;
    }
}
