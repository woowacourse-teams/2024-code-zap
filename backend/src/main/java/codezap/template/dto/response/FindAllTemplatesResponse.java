package codezap.template.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllTemplatesResponse(
        @Schema(description = "최대 페이지 개수, 최대 5개입니다.", example = "1")
        int maxPages,
        @Schema(description = "템플릿 목록")
        List<FindAllTemplateItemResponse> templates
) {
}
