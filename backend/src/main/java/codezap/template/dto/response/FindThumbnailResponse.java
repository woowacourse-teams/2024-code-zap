package codezap.template.dto.response;

import codezap.template.domain.SourceCode;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindThumbnailResponse(
        @Schema(description = "썸네일 파일명", example = "Main.java")
        String filename,

        @Schema(description = "썸네일 소스 코드", example = "public class Main { // ...")
        String content
) {
    public static FindThumbnailResponse from(SourceCode sourceCode) {
        return new FindThumbnailResponse(
                sourceCode.getFilename(),
                sourceCode.getThumbnailContent()
        );
    }
}
