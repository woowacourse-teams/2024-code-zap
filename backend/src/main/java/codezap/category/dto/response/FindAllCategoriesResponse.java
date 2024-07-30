package codezap.category.dto.response;

import java.util.List;

import codezap.category.domain.Category;

public record FindAllCategoriesResponse(
        List<FindCategoryByIdResponse> categories
) {
    public static FindAllCategoriesResponse from(List<Category> categories) {
        return new FindAllCategoriesResponse(
                categories.stream()
                        .map(FindCategoryByIdResponse::from)
                        .toList()
        );
    }
}
