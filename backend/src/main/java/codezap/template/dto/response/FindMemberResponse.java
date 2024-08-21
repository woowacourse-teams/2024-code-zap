package codezap.template.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindMemberResponse(
        @Schema(description = "회원 식별자", example = "0")
        Long id,

        @Schema(description = "회원 아이디", example = "code")
        String name
) {
}
