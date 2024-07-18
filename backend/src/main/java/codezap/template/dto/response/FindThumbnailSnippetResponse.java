package codezap.template.dto.response;

import codezap.snippet.domain.Snippet;

public record FindThumbnailSnippetResponse(
        String filename,
        String content_summary
) {
    public static FindThumbnailSnippetResponse from(Snippet snippet) {
        return new FindThumbnailSnippetResponse(
                snippet.getFilename(),
                snippet.getSummaryContent()
        );
    }
}
