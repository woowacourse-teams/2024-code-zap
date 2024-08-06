package codezap.template.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllMyTemplatesResponse(
        @Schema(description = "총 페이지", example = "5")
        int totalPage,

        @Schema(description = "템플릿 목록")
        List<FindMyTemplateResponse> templates
) {
    public static FindAllMyTemplatesResponse of(List<FindMyTemplateResponse> myTemplateResponses, int totalPage) {
        return new FindAllMyTemplatesResponse(
                totalPage,
                myTemplateResponses
        );
    }
}
