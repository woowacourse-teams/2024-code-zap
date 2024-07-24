package codezap.template.dto.request.validation;

import java.util.List;

@IncreasedIndex(message = "스니펫 순서가 잘못되었습니다.")
public interface IncreaseIndexRequest {

    List<Integer> increasedIndexes();
}