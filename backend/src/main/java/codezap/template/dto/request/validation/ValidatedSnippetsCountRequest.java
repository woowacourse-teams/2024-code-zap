package codezap.template.dto.request.validation;

import codezap.global.validation.ValidationGroups.SnippetCountGroup;

@SnippetsCount(message = "스니펫은 최소 1개 입력해야 합니다.", groups = SnippetCountGroup.class)
public interface ValidatedSnippetsCountRequest {

    Integer countSnippets();
}
