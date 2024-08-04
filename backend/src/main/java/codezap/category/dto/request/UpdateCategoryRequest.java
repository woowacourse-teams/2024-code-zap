package codezap.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateCategoryRequest(
        @Schema(description = "카테고리 이름", example = "Spring")
        @NotBlank(message = "카테고리 이름이 null 입니다.", groups = NotNullGroup.class)
        @Size(max = 255, message = "카테고리 이름은 최대 255자까지 입력 가능합니다.", groups = SizeCheckGroup.class)
        String name
) {
}
