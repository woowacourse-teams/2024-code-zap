package codezap.template.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import codezap.global.validation.ByteLength;
import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateSnippetRequest(
        @Schema(description = "파일명", example = "Main.java")
        @NotBlank(message = "파일명이 비어 있거나 공백입니다.", groups = NotNullGroup.class)
        @Size(max = 255, message = "파일 이름은 최대 255자까지 입력 가능합니다.", groups = SizeCheckGroup.class)
        String filename,

        @Schema(description = "소스 코드", example = "public class Main { // ...")
        @NotBlank(message = "소스 코드가 비어 있거나 공백입니다.", groups = NotNullGroup.class)
        @ByteLength(max = 65_535, message = "소스 코드는 최대 65,535 Byte까지 입력 가능합니다.", groups = SizeCheckGroup.class)
        String content,

        @Schema(description = "스니펫 순서", example = "1")
        @NotNull(message = "스니펫 순서가 null 입니다.")
        int ordinal
) {
}
