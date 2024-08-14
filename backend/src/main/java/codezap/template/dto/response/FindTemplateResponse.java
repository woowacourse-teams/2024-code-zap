package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.category.dto.response.FindCategoryResponse;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindTemplateResponse(
        @Schema(description = "템플릿 식별자", example = "0")
        Long id,

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

        @Schema(description = "템플릿 생성 시간", example = "2024-11-10 12:00:00", type = "string")
        LocalDateTime createdAt,
        @Schema(description = "템플릿 수정 시간", example = "2024-11-11 12:00:00", type = "string")
        LocalDateTime modifiedAt
) {
    public static FindTemplateResponse of(Template template, List<SourceCode> sourceCodes, List<Tag> tags) {
        return new FindTemplateResponse(
                template.getId(),
                template.getTitle(),
                template.getDescription(),
                mapToFindAllSourceCodeByTemplateResponse(sourceCodes),
                FindCategoryResponse.from(template.getCategory()),
                mapToFindTagByTemplateResponse(tags),
                template.getCreatedAt(),
                template.getModifiedAt()
        );
    }

    private static List<FindAllSourceCodeByTemplateResponse> mapToFindAllSourceCodeByTemplateResponse(
            List<SourceCode> sourceCodes
    ) {
        return sourceCodes.stream()
                .map(FindAllSourceCodeByTemplateResponse::from)
                .toList();
    }

    private static List<FindTagResponse> mapToFindTagByTemplateResponse(List<Tag> tags) {
        return tags.stream()
                .map(FindTagResponse::from)
                .toList();
    }
}
