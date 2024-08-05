package codezap.category.dto.response;

import java.util.List;

import codezap.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllCategoriesResponse(
        @Schema(description = "카테고리 목록")
        List<FindCategoryResponse> categories
) {
    public static FindAllCategoriesResponse from(List<Category> categories) {
        return new FindAllCategoriesResponse(
                categories.stream()
                        .map(FindCategoryResponse::from)
                        .toList()
        );
    }
}
