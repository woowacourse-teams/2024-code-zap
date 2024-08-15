package codezap.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateMemberProfileRequest(
        @Schema(description = "아이디", example = "zappy")
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        @Size(min = 2, max = 255, message = "아이디는 2~255자 사이로 입력해주세요.")
        String loginId
) {
}
