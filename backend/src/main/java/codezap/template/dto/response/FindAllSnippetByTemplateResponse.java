package codezap.template.dto.response;

import codezap.template.domain.Snippet;

public record FindAllSnippetByTemplateResponse(
        Long id,
        String filename,
        String content,
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
