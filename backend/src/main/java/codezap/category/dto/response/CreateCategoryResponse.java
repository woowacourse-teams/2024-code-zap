package codezap.category.dto.response;

import codezap.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateCategoryResponse(
        @Schema(description = "카테고리 식별자", example = "1")
        Long id,
        @Schema(description = "카테고리 이름", example = "Spring")
        String name,
        @Schema(description = "카테고리 순서", example = "1")
        Integer ordinal
) {
    public static CreateCategoryResponse from(Category category) {
        return new CreateCategoryResponse(category.getId(), category.getName(), category.getOrdinal());
    }
}
