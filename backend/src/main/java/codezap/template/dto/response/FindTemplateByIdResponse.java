package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FindTemplateByIdResponse(
        Long id,
        String title,
        FindMemberBySummaryResponse member,
        Long representative_snippet_ordinal,
        List<FindAllSnippetByTemplateResponse> snippets,
        LocalDateTime modified_at
) {
}
