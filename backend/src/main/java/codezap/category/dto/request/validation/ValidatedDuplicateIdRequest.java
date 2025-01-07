package codezap.category.dto.request.validation;

import java.util.List;

import codezap.global.validation.ValidationGroups.DuplicateIdGroup;

@DuplicateId(message = "id가 중복되었습니다.", groups = DuplicateIdGroup.class)
public interface ValidatedDuplicateIdRequest {

    List<Long> extractIds();
}
