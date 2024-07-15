package codezap.template.dto.response;

import java.util.List;

import codezap.template.dto.request.CreateSnippetRequest;

public record CreateTemplateRequest(
        String title,
        int representative_snippet_ordinal,
        List<CreateSnippetRequest> snippets
) {
}
