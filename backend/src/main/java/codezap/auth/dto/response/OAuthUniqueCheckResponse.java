package codezap.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record OAuthUniqueCheckResponse(
        @Schema(description = "회원 중복 여부", example = "true")
        boolean isDuplicated
) {
}
