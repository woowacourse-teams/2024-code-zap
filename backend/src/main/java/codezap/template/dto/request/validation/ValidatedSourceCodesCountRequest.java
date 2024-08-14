package codezap.template.dto.request.validation;

import codezap.global.validation.ValidationGroups.SourceCodeCountGroup;

@SourceCodesCount(message = "소스 코드는 최소 1개 입력해야 합니다.", groups = SourceCodeCountGroup.class)
public interface ValidatedSourceCodesCountRequest {

    Integer countSourceCodes();
}
