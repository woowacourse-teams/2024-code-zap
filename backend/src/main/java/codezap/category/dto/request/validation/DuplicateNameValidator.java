package codezap.category.dto.request.validation;

import java.util.HashSet;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DuplicateNameValidator implements ConstraintValidator<DuplicateName, ValidatedDuplicateNameRequest> {
    @Override
    public boolean isValid(ValidatedDuplicateNameRequest request,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        List<String> names = request.extractNames();
        return names.size() == new HashSet<>(names).size();
    }
}
