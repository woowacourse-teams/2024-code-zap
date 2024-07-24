package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.template.domain.ThumbnailSnippet;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllTemplatesResponse(
        @Schema(description = "템플릿 목록")
        List<ItemResponse> templates
) {
    public static FindAllTemplatesResponse from(List<ThumbnailSnippet> thumbnailSnippets) {
        List<ItemResponse> templatesBySummaryResponse = thumbnailSnippets.stream()
                .map(ItemResponse::from)
                .toList();
        return new FindAllTemplatesResponse(templatesBySummaryResponse);
    }

    public record ItemResponse(
            @Schema(description = "템플릿 식별자", example = "0")
            Long id,
            @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
            String title,
            @Schema(description = "목록 조회 시 보여질 대표 스니펫 정보")
            FindThumbnailSnippetResponse thumbnailSnippet,
            @Schema(description = "템플릿 수정 시간", example = "2024-11-11 12:00", type = "string")
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
