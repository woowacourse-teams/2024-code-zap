package codezap.template.dto.response;

import codezap.template.domain.Snippet;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindThumbnailSnippetResponse(
        @Schema(description = "썸네일 파일명", example = "Main.java")
        String filename,

        @Schema(description = "썸네일 소스 코드", example = "public class Main { // ...")
        String content
) {
    public static FindThumbnailSnippetResponse from(Snippet snippet) {
        return new FindThumbnailSnippetResponse(
                snippet.getFilename(),
                snippet.getThumbnailContent()
        );
    }
}
