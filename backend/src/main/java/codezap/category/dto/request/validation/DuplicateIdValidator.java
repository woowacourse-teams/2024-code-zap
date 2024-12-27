package codezap.category.dto.request.validation;

import java.util.HashSet;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DuplicateIdValidator implements ConstraintValidator<DuplicateId, ValidatedDuplicateIdRequest> {

    @Override
    public boolean isValid(ValidatedDuplicateIdRequest request,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        List<Long> ids = request.extractIds();
        return ids.size() == new HashSet<>(ids).size();
    }
}
