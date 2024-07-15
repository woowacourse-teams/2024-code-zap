package codezap.template.dto.response;

import java.time.LocalDateTime;

public record FindTemplateBySummaryResponse(
        Long id,
        String title,
        FindMemberBySummaryResponse member,
        FindRepresentativeSnippetResponse representative_snippet,
        LocalDateTime modified_at
) {
}
