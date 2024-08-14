package codezap.template.dto.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SourceCodesCountValidator implements ConstraintValidator<SourceCodesCount, ValidatedSourceCodesCountRequest> {

    @Override
    public boolean isValid(ValidatedSourceCodesCountRequest validatedSourceCodesCountRequest,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        Integer sourcesCount = validatedSourceCodesCountRequest.countSourceCodes();
        return sourcesCount > 0;
    }
}
