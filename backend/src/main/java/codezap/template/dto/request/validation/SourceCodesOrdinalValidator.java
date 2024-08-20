package codezap.template.dto.request.validation;

import java.util.List;
import java.util.stream.IntStream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SourceCodesOrdinalValidator implements
        ConstraintValidator<SourceCodesOrdinal, ValidatedSourceCodesOrdinalRequest> {

    @Override
    public boolean isValid(ValidatedSourceCodesOrdinalRequest validatedSourceCodesOrdinalRequest,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        List<Integer> indexes = validatedSourceCodesOrdinalRequest.extractSourceCodesOrdinal();
        return IntStream.range(0, indexes.size())
                .allMatch(index -> indexes.get(index) == index + 1);
    }
}
