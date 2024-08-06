package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllTemplatesResponse(
        @Schema(description = "전체 페이지 개수", example = "1")
        int totalPage,
        @Schema(description = "총 템플릿 개수", example = "134")
        long totalElement,
        @Schema(description = "템플릿 목록")
        List<ItemResponse> templates
) {

    public record ItemResponse(
            @Schema(description = "템플릿 식별자", example = "0")
            Long id,
            @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
            String title,
            @Schema(description = "템플릿 설명", example = "Jwt 토큰을 이용하여 로그인 기능을 구현합니다.")
            String description,
            @Schema(description = "태그 리스트")
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
