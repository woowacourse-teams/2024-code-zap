package codezap.template.dto.response;

import codezap.template.domain.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindTagByIdResponse(
        @Schema(description = "태그 식별자", example = "1")
        Long id,

        @Schema(description = "태그 이름", example = "스프링")
        String name
) {
    public static FindTagByIdResponse from(Tag tag) {
        return new FindTagByIdResponse(tag.getId(), tag.getName());
    }
}
