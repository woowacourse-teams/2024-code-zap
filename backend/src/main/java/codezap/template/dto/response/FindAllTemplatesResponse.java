package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.template.domain.ThumbnailSnippet;

public record FindAllTemplatesResponse(
        List<ItemResponse> templates
) {
    public static FindAllTemplatesResponse from(List<ThumbnailSnippet> thumbnailSnippets) {
        List<ItemResponse> templatesBySummaryResponse = thumbnailSnippets.stream()
                .map(ItemResponse::from)
                .toList();
        return new FindAllTemplatesResponse(templatesBySummaryResponse);
    }

    public record ItemResponse(
            Long id,
            String title,
            FindThumbnailSnippetResponse thumbnailSnippet,
            LocalDateTime modifiedAt
    ) {
        public static ItemResponse from(ThumbnailSnippet thumbnailSnippet) {
            return new ItemResponse(
                    thumbnailSnippet.getTemplate().getId(),
                    thumbnailSnippet.getTemplate().getTitle(),
                    FindThumbnailSnippetResponse.from(thumbnailSnippet.getSnippet()),
                    thumbnailSnippet.getModifiedAt()
            );
        }
    }
}
