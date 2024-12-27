package codezap.template.dto.request.validation;

import java.util.List;

import codezap.global.validation.ValidationGroups.OrdinalGroup;

@Ordinal(message = "순서가 잘못되었습니다.", groups = OrdinalGroup.class)
public interface ValidatedOrdinalRequest {

    List<Integer> extractOrdinal();
}
