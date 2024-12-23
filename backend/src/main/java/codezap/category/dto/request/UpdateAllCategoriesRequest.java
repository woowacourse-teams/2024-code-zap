package codezap.category.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import codezap.global.validation.ValidationGroups.NotNullGroup;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateAllCategoriesRequest(
        @Schema(description = "생성할 카테고리 목록")
        @Valid
        List<CreateCategoryRequest> createCategories,

        @Schema(description = "수정할 카테고리 목록")
        @Valid
        List<UpdateCategoryRequest> updateCategories,

        @Schema(description = "삭제할 카테고리 목록")
        @NotNull(message = "삭제하는 카테고리 ID 목록이 null 입니다.", groups = NotNullGroup.class)
        List<Long> deleteCategoryIds
) {
}
