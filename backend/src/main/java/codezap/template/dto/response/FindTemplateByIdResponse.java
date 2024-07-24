package codezap.template.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import codezap.template.domain.Snippet;
import codezap.template.domain.Template;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindTemplateByIdResponse(
        @Schema(description = "템플릿 식별자", example = "0")
        Long id,
        @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
        String title,
        @Schema(description = "스니펫 목록")
        List<FindAllSnippetByTemplateResponse> snippets,
        @Schema(description = "템플릿 수정 시간", example = "2024-11-11 12:00", type = "string")
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
