package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.snippet.domain.Snippet;
import codezap.template.domain.Template;

public record FindTemplateByIdResponse(
        Long id,
        String title,
        FindMemberBySummaryResponse member,
        Integer representative_snippet_ordinal,
        List<FindAllSnippetByTemplateResponse> snippets,
        LocalDateTime modified_at
) {
    public static FindTemplateByIdResponse from(Template template, List<Snippet> snippets) {
        return new FindTemplateByIdResponse(
                template.getId(),
                template.getTitle(),
                FindMemberBySummaryResponse.from(template.getMember()),
                1,
                mapToFindAllSnippetByTemplateResponse(snippets),
                template.getModifiedAt()
        );
    }

    private static List<FindAllSnippetByTemplateResponse> mapToFindAllSnippetByTemplateResponse(
            List<Snippet> snippets
    ) {
        return snippets.stream()
                .map(FindAllSnippetByTemplateResponse::from)
                .toList();
    }
}
