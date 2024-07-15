package codezap.template.dto.response;

import codezap.snippet.domain.Snippet;

public record FindRepresentativeSnippetResponse(
        String filename,
        String content_summary
) {
    public static FindRepresentativeSnippetResponse from(Snippet snippet) {
        return new FindRepresentativeSnippetResponse(
                snippet.getFilename(),
                snippet.getSummaryContent()
        );
    }
}
