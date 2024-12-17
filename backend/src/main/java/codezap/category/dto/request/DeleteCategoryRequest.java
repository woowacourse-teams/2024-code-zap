package codezap.category.dto.request;

import jakarta.validation.constraints.NotNull;

import codezap.global.validation.ValidationGroups.NotNullGroup;
import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteCategoryRequest(
        @Schema(description = "카테고리 ID", example = "0")
        @NotNull(message = "카테고리 ID가 null 입니다.", groups = NotNullGroup.class)
        Long id,

        @Schema(description = "카테고리 순서", example = "1")
        @NotNull(message = "카테고리 순서가 null 입니다.", groups = NotNullGroup.class)
        Long ordinal
) {
}
