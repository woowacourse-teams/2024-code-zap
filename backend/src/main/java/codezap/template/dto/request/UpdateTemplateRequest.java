package codezap.template.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateTemplateRequest(
        @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
        @NotNull(message = "템플릿 이름이 null 입니다.")
        String title,

        @Schema(description = "새로 추가한 스니펫 내역")
        @NotNull(message = "createSnippets 리스트가 null 입니다.")
        List<CreateSnippetRequest> createSnippets,

        @Schema(description = "삭제, 생성 스니펫을 제외한 모든 스니펫 내역")
        @NotNull(message = "updateSnippets 리스트가 null 입니다.")
        List<UpdateSnippetRequest> updateSnippets,

        @Schema(description = "삭제한 스니펫 식별자")
        @NotNull(message = "deleteSnippetIds 리스트가 null 입니다.")
        List<Long> deleteSnippetIds
) {
}
