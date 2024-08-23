package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.template.domain.Thumbnail;
import io.swagger.v3.oas.annotations.media.Schema;

public record ExploreTemplatesResponse(
        @Schema(description = "템플릿 목록")
        List<ItemResponse> templates
) {
    public static ExploreTemplatesResponse from(List<Thumbnail> thumbnails) {
        List<ItemResponse> templatesBySummaryResponse = thumbnails.stream()
                .map(ItemResponse::from)
                .toList();
        return new ExploreTemplatesResponse(templatesBySummaryResponse);
    }

    public record ItemResponse(
            @Schema(description = "템플릿 식별자", example = "0")
            Long id,
            @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
            String title,
            @Schema(description = "목록 조회 시 보여질 대표 소스 코드 정보")
            FindThumbnailResponse thumbnail,
            @Schema(description = "템플릿 수정 시간", example = "2024-11-11 12:00", type = "string")
            LocalDateTime modifiedAt
    ) {
        public static ItemResponse from(Thumbnail thumbnail) {
            return new ItemResponse(
                    thumbnail.getTemplate().getId(),
                    thumbnail.getTemplate().getTitle(),
                    FindThumbnailResponse.from(thumbnail.getSourceCode()),
                    thumbnail.getModifiedAt()
            );
        }
    }
}
