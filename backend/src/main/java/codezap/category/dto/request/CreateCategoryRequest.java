package codezap.category.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotNull(message = "카테고리 이름이 null 입니다.")
        @Size(max = 255, message = "카테고리 이름은 최대 255자까지 입력 가능합니다.")
        String name
) {
}
