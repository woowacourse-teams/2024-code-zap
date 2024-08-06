package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllTemplatesResponse(
        @Schema(description = "전체 페이지 개수", example = "1")
        int totalPage,
        @Schema(description = "템플릿 목록")
        List<ItemResponse> templates
) {

    public record ItemResponse(
            @Schema(description = "템플릿 식별자", example = "0")
            Long id,
            @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
            String title,
            //todo 문서화
            String description,
            //todo 문서화
            List<FindTagResponse> tags,
            @Schema(description = "템플릿 수정 시간", example = "2024-11-11 12:00", type = "string")
            LocalDateTime modifiedAt
    ) {
        public static ItemResponse of(Template template, List<Tag> templateTags) {
        return new ItemResponse(
                template.getId(),
                template.getTitle(),
                template.getDescription(),
                templateTags.stream()
                        .map(tag -> new FindTagResponse(tag.getId(), tag.getName()))
                        .toList(),
                template.getModifiedAt()
        );
    }
    }
}
