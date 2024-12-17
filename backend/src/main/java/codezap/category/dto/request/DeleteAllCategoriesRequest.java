package codezap.category.dto.request;

import java.util.List;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteAllCategoriesRequest(
        @Schema(description = "삭제할 카테고리 목록")
        @Valid
        List<DeleteCategoryRequest> categories
) {
}
