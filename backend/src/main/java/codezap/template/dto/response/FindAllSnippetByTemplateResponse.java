package codezap.template.dto.response;

import codezap.template.domain.Snippet;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllSnippetByTemplateResponse(
        @Schema(description = "파일 식별자", example = "0")
        Long id,

        @Schema(description = "파일 이름", example = "Main.java")
        String filename,

        @Schema(description = "소스 코드", example = "public class Main { // ...")
        String content,

        @Schema(description = "스니펫의 순서", example = "1")
        int ordinal
) {
    public static FindAllSnippetByTemplateResponse from(Snippet snippet) {
        return new FindAllSnippetByTemplateResponse(
                snippet.getId(),
                snippet.getFilename(),
                snippet.getContent(),
                snippet.getOrdinal()
        );
    }
}
