package codezap.template.dto.response;

import java.time.LocalDateTime;

import codezap.template.domain.ThumbnailSnippet;

public record FindTemplateBySummaryResponse(
        Long id,
        String title,
        FindThumbnailSnippetResponse thumbnailSnippet,
        LocalDateTime modifiedAt
) {
    public static FindTemplateBySummaryResponse from(ThumbnailSnippet thumbnailSnippet) {
        return new FindTemplateBySummaryResponse(
                thumbnailSnippet.getTemplate().getId(),
                thumbnailSnippet.getTemplate().getTitle(),
                FindThumbnailSnippetResponse.from(thumbnailSnippet.getSnippet()),
                thumbnailSnippet.getModifiedAt()
        );
    }
}
