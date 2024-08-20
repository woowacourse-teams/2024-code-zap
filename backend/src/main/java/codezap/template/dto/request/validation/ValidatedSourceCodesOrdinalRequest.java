package codezap.template.dto.request.validation;

import java.util.List;

import codezap.global.validation.ValidationGroups.SourceCodeOrdinalGroup;

@SourceCodesOrdinal(message = "소스 코드 순서가 잘못되었습니다.", groups = SourceCodeOrdinalGroup.class)
public interface ValidatedSourceCodesOrdinalRequest {

    List<Integer> extractSourceCodesOrdinal();
}
