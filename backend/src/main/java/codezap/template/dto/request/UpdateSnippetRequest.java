package codezap.template.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import codezap.global.validation.ByteLength;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateSnippetRequest(
        @Schema(description = "파일 식별자", example = "0")
        @NotNull(message = "파일 id가 null 입니다.")
        Long id,

        @Schema(description = "파일 이름", example = "Main.java")
        @NotNull(message = "파일 이름이 null 입니다.")
        @Size(max = 255, message = "파일 이름은 최대 255자까지 입력 가능합니다.")
        String filename,

        @Schema(description = "소스 코드", example = "public class Main { // ...")
        @NotNull(message = "파일 내용이 null 입니다.")
        @ByteLength(max = 65_535, message = "파일 내용은 최대 65,535 Byte까지 입력 가능합니다.")
        String content,

        @Schema(description = "스니펫 순서", example = "1")
        @NotNull(message = "스니펫 순서가 null 입니다.")
        int ordinal
) {
}
