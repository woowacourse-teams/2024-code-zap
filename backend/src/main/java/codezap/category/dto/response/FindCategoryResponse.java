package codezap.category.dto.response;

import codezap.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindCategoryResponse(
        @Schema(description = "카테고리 식별자", example = "1")
        Long id,
        @Schema(description = "카테고리 이름", example = "Spring")
        String name,
        @Schema(description = "카테고리 순서", example = "1")
        int ordinal,
        @Schema(description = "카테고리 개수", example = "1")
        long count
) {
    public static FindCategoryResponse from(Category category) {
        return new FindCategoryResponse(category.getId(), category.getName(), category.getOrdinal(), category.getTemplateCount());
    }
}
