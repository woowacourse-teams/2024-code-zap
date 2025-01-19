package codezap.global.validation;

import java.util.List;
import java.util.stream.IntStream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrdinalValidator implements
        ConstraintValidator<Ordinal, ValidatedOrdinalRequest> {

    @Override
    public boolean isValid(ValidatedOrdinalRequest validatedOrdinalRequest,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        List<Integer> indexes = validatedOrdinalRequest.extractOrdinal();
        return IntStream.range(0, indexes.size())
                .allMatch(index -> indexes.get(index) == index + 1);
    }
}
