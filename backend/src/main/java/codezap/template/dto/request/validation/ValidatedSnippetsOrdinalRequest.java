package codezap.template.dto.request.validation;

import java.util.List;

@SnippetsOrdinal(message = "스니펫 순서가 잘못되었습니다.")
public interface ValidatedSnippetsOrdinalRequest {

    List<Integer> extractSnippetsOrdinal();
}