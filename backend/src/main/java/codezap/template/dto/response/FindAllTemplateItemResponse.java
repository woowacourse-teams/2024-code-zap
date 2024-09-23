package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.member.domain.Member;
import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindTagResponse;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
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
            Template template, List<Tag> templateTags, SourceCode thumbnailSourceCode, Boolean isLiked
    ) {
        return new FindAllTemplateItemResponse(
                template.getId(),
                null,
                template.getTitle(),
                template.getDescription(),
                templateTags.stream()
                        .map(tag -> new FindTagResponse(tag.getId(), tag.getName()))
                        .toList(),
                FindThumbnailResponse.from(thumbnailSourceCode),
                template.getLikesCount(),
                isLiked,
                template.getCreatedAt(),
                template.getModifiedAt()
        );
    }

    public FindAllTemplateItemResponse updateMember(Member member) {
        return new FindAllTemplateItemResponse(
                id,
                new FindMemberResponse(member.getId(), member.getName()),
                title,
                description,
                tags,
                thumbnail,
                likesCount,
                isLiked,
                createdAt,
                modifiedAt
        );
    }
}
