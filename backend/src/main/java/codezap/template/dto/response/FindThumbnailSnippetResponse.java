package codezap.template.dto.response;

import codezap.template.domain.Snippet;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindThumbnailSnippetResponse(
        @Schema(description = "파일 이름", example = "Main.java")
        String filename,
        @Schema(description = "목록 조회 시 보여질 코드", example = "public class Main { // ...")
        String thumbnailContent
) {
    public static FindThumbnailSnippetResponse from(Snippet snippet) {
        return new FindThumbnailSnippetResponse(
                snippet.getFilename(),
                snippet.getThumbnailContent()
        );
    }
}
