package codezap.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

public record OAuthUniqueCheckRequest(
        @Schema(description = "OAuth 제공자", example = "Github")
        @NotBlank(message = "OAuth 제공자가 입력되지 않았습니다.")
        String oauthProvider,

        @Schema(description = "authorization code", example = "07a1338bd35812d7f8a7")
        @NotBlank(message = "authorization code를 입력해주세요")
        String authorizationCode
) {
}
