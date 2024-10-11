package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.category.dto.response.FindCategoryResponse;
import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindTagResponse;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindTemplateResponse(
        @Schema(description = "템플릿 식별자", example = "0")
        Long id,

        @Schema(description = "회원 설명")
        FindMemberResponse member,

        @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
        String title,

        @Schema(description = "템플릿 설명", example = "JWT를 사용하여 로그인 기능을 구현함")
        String description,

        @Schema(description = "소스 코드 목록")
        List<FindAllSourceCodeByTemplateResponse> sourceCodes,

        @Schema(description = "카테고리 정보")
        FindCategoryResponse category,

        @Schema(description = "태그 목록")
        List<FindTagResponse> tags,

        @Schema(description = "공개 범위", example = "PUBLIC")
        Visibility visibility,

        @Schema(description = "좋아요 수", example = "134")
        Long likesCount,

        @Schema(description = "조회 회원의 좋아요 여부", example = "true")
        Boolean isLiked,

        @Schema(description = "템플릿 생성 시간", example = "2024-11-10 12:00:00", type = "string")
        LocalDateTime createdAt,

        @Schema(description = "템플릿 수정 시간", example = "2024-11-11 12:00:00", type = "string")
        LocalDateTime modifiedAt
) {
    public static FindTemplateResponse of(
            Template template,
            List<SourceCode> sourceCodes,
            List<Tag> tags,
            Boolean isLiked
    ) {
        return new FindTemplateResponse(
                template.getId(),
                FindMemberResponse.from(template.getMember()),
                template.getTitle(),
                template.getDescription(),
                sourceCodes.stream()
                        .map(FindAllSourceCodeByTemplateResponse::from)
                        .toList(),
                FindCategoryResponse.from(template.getCategory()),
                tags.stream()
                        .map(FindTagResponse::from)
                        .toList(),
                template.getVisibility(),
                template.getLikesCount(),
                isLiked,
                template.getCreatedAt(),
                template.getModifiedAt()
        );
    }
}
