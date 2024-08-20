package codezap.template.dto.response;

import java.util.List;

import codezap.representative_snippet.domain.RepresentativeSnippet;

public record FindAllTemplatesResponse(
        List<FindTemplateBySummaryResponse> templates
) {
    public static FindAllTemplatesResponse from(List<RepresentativeSnippet> representativeSnippets) {
        List<FindTemplateBySummaryResponse> templatesBySummaryResponse = representativeSnippets.stream()
                .map(FindTemplateBySummaryResponse::from)
                .toList();
        return new FindAllTemplatesResponse(templatesBySummaryResponse);
    }
}
