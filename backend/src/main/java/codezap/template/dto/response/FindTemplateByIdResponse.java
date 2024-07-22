package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.template.domain.Snippet;
import codezap.template.domain.Template;

public record FindTemplateByIdResponse(
        Long id,
        String title,
        List<FindAllSnippetByTemplateResponse> snippets,
        LocalDateTime modifiedAt
) {
    public static FindTemplateByIdResponse of(Template template, List<Snippet> snippets) {
        return new FindTemplateByIdResponse(
                template.getId(),
                template.getTitle(),
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
