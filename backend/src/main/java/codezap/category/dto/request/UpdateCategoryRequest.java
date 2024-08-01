package codezap.category.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateCategoryRequest(
        @Schema(description = "카테고리 식별자", example = "1")
        @NotNull(message = "카테고리 id가 null 입니다.")
        Long id,
        @Schema(description = "카테고리 이름", example = "Spring")
        @NotNull(message = "카테고리 이름이 null 입니다.")
        @Size(max = 255, message = "카테고리 이름은 최대 255자까지 입력 가능합니다.")
        String name
) {
}
