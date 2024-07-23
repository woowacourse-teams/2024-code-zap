package codezap.template.dto.request.validation;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import codezap.template.dto.request.CreateSnippetRequest;

public class IncreasedIndexValidator implements ConstraintValidator<IncreasedIndex, List<CreateSnippetRequest>> {
    @Override
    public boolean isValid(List<CreateSnippetRequest> createSnippetRequests,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        for(int index = 0; index < createSnippetRequests.size(); index++) {
            if(createSnippetRequests.get(index).ordinal() != index + 1) {
                return false;
            }
        }

        return true;
    }
}
