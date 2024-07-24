package codezap.template.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateTemplateRequest(
        @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
        String title,

        @Schema(description = "새로 추가한 스니펫 내역")
        List<CreateSnippetRequest> createSnippets,

        @Schema(description = "삭제, 생성 스니펫을 제외한 모든 스니펫 내역")
        List<UpdateSnippetRequest> updateSnippets,

        @Schema(description = "삭제한 스니펫 식별자")
        List<Long> deleteSnippetIds
) {
}
