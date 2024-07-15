package codezap.template.dto.response;

import java.time.LocalDateTime;

import codezap.representative_snippet.domain.RepresentativeSnippet;
import codezap.template.domain.Template;

public record FindTemplateBySummaryResponse(
        Long id,
        String title,
        FindMemberBySummaryResponse member,
        FindRepresentativeSnippetResponse representative_snippet,
        LocalDateTime modified_at
) {
    public static FindTemplateBySummaryResponse from(RepresentativeSnippet representativeSnippet) {
        return new FindTemplateBySummaryResponse(
                representativeSnippet.getTemplate().getId(),
                representativeSnippet.getTemplate().getTitle(),
                FindMemberBySummaryResponse.from(representativeSnippet.getTemplate().getMember()),
                FindRepresentativeSnippetResponse.from(representativeSnippet.getSnippet()),
                representativeSnippet.getModifiedAt()
        );
    }
}
