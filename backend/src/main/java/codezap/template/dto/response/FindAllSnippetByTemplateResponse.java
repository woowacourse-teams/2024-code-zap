package codezap.template.dto.response;

public record FindAllSnippetByTemplateResponse(
        Long id,
        String filename,
        String content,
        int ordinal
) {
}
