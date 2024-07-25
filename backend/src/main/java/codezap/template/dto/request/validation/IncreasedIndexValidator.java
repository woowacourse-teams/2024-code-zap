package codezap.template.dto.request.validation;

import java.util.stream.IntStream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IncreasedIndexValidator implements ConstraintValidator<IncreasedIndex, IncreaseIndexRequest> {

    @Override
    public boolean isValid(IncreaseIndexRequest increaseIndexRequest,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        return IntStream.range(0, increaseIndexRequest.increasedIndexes().size())
                .allMatch(index -> increaseIndexRequest.increasedIndexes().get(index) == index + 1);
    }
}
