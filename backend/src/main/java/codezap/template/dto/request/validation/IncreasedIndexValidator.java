package codezap.template.dto.request.validation;

import java.util.List;
import java.util.stream.IntStream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import codezap.template.dto.request.CreateSnippetRequest;

public class IncreasedIndexValidator implements ConstraintValidator<IncreasedIndex, List<CreateSnippetRequest>> {
    @Override
    public boolean isValid(List<CreateSnippetRequest> createSnippetRequests,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        return IntStream.range(0, createSnippetRequests.size())
                .allMatch(index -> createSnippetRequests.get(index).ordinal() == index + 1);
    }
}
