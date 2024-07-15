package codezap.template.dto.response;

import java.util.List;

public record FindAllTemplatesResponse(
        List<FindTemplateBySummaryResponse> templates
) {
}
