package codezap.template.dto.request;

import java.util.List;

public record CreateTemplateRequest(
        String title,
        List<CreateSnippetRequest> snippets
) {
}
