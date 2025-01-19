package codezap.category.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateCategoryRequest(
        @Schema(description = "카테고리 이름", example = "Spring")
        @NotBlank(message = "카테고리 이름이 null 입니다.", groups = NotNullGroup.class)
        @Size(max = 15, message = "카테고리 이름은 최대 15자까지 입력 가능합니다.", groups = SizeCheckGroup.class)
        String name,

        @Schema(description = "카테고리 순서", example = "1")
        @NotNull(message = "카테고리 순서가 null 입니다.", groups = NotNullGroup.class)
        @Min(value = 1, message = "카테고리의 순서는 1 이상이어야 합니다.")
        Integer ordinal
) {
}
