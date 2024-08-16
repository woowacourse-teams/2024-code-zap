package codezap.template.dto.response;

import codezap.template.domain.SourceCode;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllSourceCodeByTemplateResponse(
        @Schema(description = "파일 식별자", example = "0")
        Long id,

        @Schema(description = "파일 이름", example = "Main.java")
        String filename,

        @Schema(description = "소스 코드", example = "public class Main { // ...")
        String content,

        @Schema(description = "소스 코드 순서", example = "1")
        int ordinal
) {
    public static FindAllSourceCodeByTemplateResponse from(SourceCode sourceCode) {
        return new FindAllSourceCodeByTemplateResponse(
                sourceCode.getId(),
                sourceCode.getFilename(),
                sourceCode.getContent(),
                sourceCode.getOrdinal()
        );
    }
}
