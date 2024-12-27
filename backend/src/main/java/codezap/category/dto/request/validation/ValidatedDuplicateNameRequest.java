package codezap.category.dto.request.validation;

import java.util.List;

import codezap.global.validation.ValidationGroups.DuplicateNameGroup;

@DuplicateName(message = "카테고리명이 중복되었습니다.", groups = DuplicateNameGroup.class)
public interface ValidatedDuplicateNameRequest {

    List<String> extractNames();
}
