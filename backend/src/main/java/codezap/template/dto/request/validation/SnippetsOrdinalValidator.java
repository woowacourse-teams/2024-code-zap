package codezap.template.dto.request.validation;

import java.util.List;
import java.util.stream.IntStream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SnippetsOrdinalValidator implements ConstraintValidator<SnippetsOrdinal, ValidatedSnippetsOrdinalRequest> {

    @Override
    public boolean isValid(ValidatedSnippetsOrdinalRequest validatedSnippetsOrdinalRequest,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        List<Integer> indexes = validatedSnippetsOrdinalRequest.extractSnippetsOrdinal();
        return IntStream.range(0, indexes.size())
                .allMatch(index -> indexes.get(index) == index + 1);
    }
}
