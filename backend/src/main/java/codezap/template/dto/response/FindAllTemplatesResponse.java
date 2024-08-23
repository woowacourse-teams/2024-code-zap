package codezap.template.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllTemplatesResponse(
        @Schema(description = "전체 페이지 개수", example = "1")
        int totalPages,
        @Schema(description = "총 템플릿 개수", example = "134")
        long totalElements,
        @Schema(description = "템플릿 목록")
        List<FindAllTemplateItemResponse> templates
) {
    public FindAllTemplatesResponse updateTemplates(List<FindAllTemplateItemResponse> templates) {
        return new FindAllTemplatesResponse(totalPages, totalElements, templates);
    }
}
