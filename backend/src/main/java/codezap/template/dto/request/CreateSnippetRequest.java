package codezap.template.dto.request;

public record CreateSnippetRequest(
        String filename,
        String content,
        int ordinal
) {
}
