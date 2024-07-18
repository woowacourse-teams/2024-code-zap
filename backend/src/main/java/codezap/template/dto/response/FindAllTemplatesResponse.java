package codezap.template.dto.response;

import java.util.List;

import codezap.thumbnailsnippet.domain.ThumbnailSnippet;

public record FindAllTemplatesResponse(
        List<FindTemplateBySummaryResponse> templates
) {
    public static FindAllTemplatesResponse from(List<ThumbnailSnippet> thumbnailSnippets) {
        List<FindTemplateBySummaryResponse> templatesBySummaryResponse = thumbnailSnippets.stream()
                .map(FindTemplateBySummaryResponse::from)
                .toList();
        return new FindAllTemplatesResponse(templatesBySummaryResponse);
    }
}
