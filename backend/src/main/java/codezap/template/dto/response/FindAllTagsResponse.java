package codezap.template.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllTagsResponse(
        @Schema(description = "태그 목록")
        List<FindTagResponse> tags
) {
}
