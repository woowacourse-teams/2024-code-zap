package codezap.category.dto.request;

import java.util.List;
import java.util.stream.Stream;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import codezap.category.dto.request.validation.ValidatedDuplicateNameRequest;
import codezap.category.dto.request.validation.ValidatedDuplicateIdRequest;
import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidatedOrdinalRequest;
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
) implements ValidatedOrdinalRequest, ValidatedDuplicateIdRequest, ValidatedDuplicateNameRequest {
        @Override
        public List<Integer> extractOrdinal() {
                return Stream.concat(
                        createCategories.stream().map(CreateCategoryRequest::ordinal),
                        updateCategories.stream().map(UpdateCategoryRequest::ordinal)
                ).sorted().toList();
        }

        @Override
        public List<Long> extractIds() {
                return Stream.concat(
                        updateCategories.stream().map(UpdateCategoryRequest::id),
                        deleteCategoryIds.stream()
                ).toList();
        }

        @Override
        public List<String> extractNames() {
                return Stream.concat(
                        createCategories.stream().map(CreateCategoryRequest::name),
                        updateCategories.stream().map(UpdateCategoryRequest::name)
                ).toList();
        }
}
