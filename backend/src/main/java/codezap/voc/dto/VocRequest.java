package codezap.voc.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

public record VocRequest(

        @Schema(description = "문의 내용", example = "코드잽 정말 잘 사용하고 있어요. 우테코 6기 코드잽 화이팅!")
        @NotEmpty(message = "문의 내용은 비어있을 수 없습니다.")
        @Size(min = 20, max = 10_000, message = "문의 내용은 최소 20자, 최대 10,000 자 입력할 수 있습니다.")
        String message,

        @Schema(description = "이메일(선택)", example = "codezap2024@gmail.com")
        @Email(regexp = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "올바른 형식의 이메일 주소여야 합니다.") // Regex from RFC 5322
        String email,

        @Nullable
        @Schema(description = "사용자 db id(선택)", example = "1")
        @Min(value = 1, message = "1 이상이어야 합니다.")
        Long memberId,

        @Nullable
        @Schema(description = "사용자 아이디(선택)", example = "만두")
        @Size(max = 255, message = "아이디는 255자 이하로 입력해주세요.")
        String name
) {
    public VocRequest(String message, String email) {
        this(message, email, null, null);
    }
}
