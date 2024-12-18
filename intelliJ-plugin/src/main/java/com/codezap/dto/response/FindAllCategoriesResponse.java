package com.codezap.dto.response;

import java.util.List;

public record FindAllCategoriesResponse(
        List<FindCategoryResponse> categories
) {
    public String[] getCategoryNames() {
        return categories.stream()
                .map(FindCategoryResponse::name)
                .toArray(String[]::new);
    }

    public long getId(String name) {
        FindCategoryResponse category = categories.stream()
                .filter(c -> c.name().equals(name))
                .findAny()
                .orElseThrow();

        return category.id();
    }
}
