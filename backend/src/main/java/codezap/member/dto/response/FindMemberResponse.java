package codezap.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindMemberResponse(
        @Schema(description = "이메일", example = "code@zap.com")
        String email,

        @Schema(description = "사용자명", example = "zappy")
        String username
) {
}
