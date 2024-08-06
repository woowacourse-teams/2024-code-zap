package codezap.template.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllMyTemplatesResponse(
        @Schema(description = "템플릿 목록")
        List<FindMyTemplateResponse> templates
) {
    public static FindAllMyTemplatesResponse from(List<FindMyTemplateResponse> myTemplateResponses) {
        return new FindAllMyTemplatesResponse(
                myTemplateResponses
        );
    }
}
