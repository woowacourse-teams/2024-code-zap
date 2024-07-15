package codezap.template.dto.request;

import java.util.List;

public record CreateTemplateRequest(
        String title,
        int representative_snippet_ordinal,
        List<CreateSnippetRequest> snippets
) {
}
