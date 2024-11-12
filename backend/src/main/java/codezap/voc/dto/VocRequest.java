package codezap.voc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

public record VocRequest(

        @Schema(description = "문의 내용", example = "코드잽 정말 잘 사용하고 있어요.")
        @NotEmpty(message = "문의 내용은 비어있을 수 없습니다.")
        @Size(min = 20, max = 10_000, message = "문의 내용은 최소 20자, 최대 10,000 자 입력할 수 있습니다.")
        String content,

        @Schema(description = "이메일(선택)", example = "codezap2024@gmail.com")
        @Email(regexp = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "올바른 형식의 이메일 주소여야 합니다.") // Regex from RFC 5322
        String email
) {
}
