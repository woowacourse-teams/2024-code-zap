package codezap.category.dto.response;

import codezap.category.domain.Category;

public record FindCategoryByIdResponse(
        Long id,
        String name
) {
    public static FindCategoryByIdResponse from(Category category) {
        return new FindCategoryByIdResponse(category.getId(), category.getName());
    }
}
