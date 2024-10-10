package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindTagResponse;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllTemplateItemResponse(
        @Schema(description = "템플릿 ID", example = "0")
        Long id,

        @Schema(description = "회원 설명")
        FindMemberResponse member,

        @Schema(description = "템플릿명", example = "스프링 로그인 구현")
        String title,

        @Schema(description = "템플릿 설명", example = "Jwt 토큰을 이용하여 로그인 기능을 구현합니다.")
        String description,

        @Schema(description = "태그 목록")
        List<FindTagResponse> tags,

        @Schema(description = "썸네일")
        FindThumbnailResponse thumbnail,

        @Schema(description = "공개 범위", example = "PUBLIC")
        Visibility visibility,

        @Schema(description = "좋아요 수", example = "134")
        Long likesCount,
        @Schema(description = "조회 멤버의 좋아요 여부", example = "true")
        Boolean isLiked,

        @Schema(description = "템플릿 생성 시간", example = "2024-11-10 12:00:00", type = "string")
        LocalDateTime createdAt,

        @Schema(description = "템플릿 수정 시간", example = "2024-11-11 12:00:00", type = "string")
        LocalDateTime modifiedAt
) {
    public static FindAllTemplateItemResponse of(
            Template template,
            List<Tag> tags,
            SourceCode thumbnailSourceCode,
            Boolean isLiked
    ) {
        return new FindAllTemplateItemResponse(
                template.getId(),
                FindMemberResponse.from(template.getMember()),
                template.getTitle(),
                template.getDescription(),
                tags.stream()
                        .map(FindTagResponse::from)
                        .toList(),
                FindThumbnailResponse.from(thumbnailSourceCode),
                template.getVisibility(),
                template.getLikesCount(),
                isLiked,
                template.getCreatedAt(),
                template.getModifiedAt()
        );
    }
}
