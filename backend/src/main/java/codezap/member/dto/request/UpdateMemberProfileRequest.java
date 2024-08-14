package codezap.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateMemberProfileRequest(
        @Schema(description = "이메일", example = "code@zap.com")
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        @Size(max = 255, message = "이메일은 255자 이하로 입력해주세요.")
        String email,

        @Schema(description = "사용자명", example = "zappy")
        @NotBlank(message = "사용자명이 입력되지 않았습니다.")
        @Size(min = 2, max = 255, message = "사용자명은 2~255자 사이로 입력해주세요.")
        String username
) {
}
