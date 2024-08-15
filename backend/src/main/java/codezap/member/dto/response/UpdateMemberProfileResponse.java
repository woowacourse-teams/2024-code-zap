package codezap.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateMemberProfileResponse(
        @Schema(description = "사용자명", example = "zappy")
        String username
) {
}
