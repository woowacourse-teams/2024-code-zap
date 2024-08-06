package codezap.template.dto.request.validation;

import java.util.List;

import codezap.global.validation.ValidationGroups.SnippetOrdinalGroup;

@SnippetsOrdinal(message = "스니펫 순서가 잘못되었습니다.", groups = SnippetOrdinalGroup.class)
public interface ValidatedSnippetsOrdinalRequest {

    List<Integer> extractSnippetsOrdinal();
}