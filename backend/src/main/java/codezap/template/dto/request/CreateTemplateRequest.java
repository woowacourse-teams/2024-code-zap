package codezap.template.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import codezap.template.dto.request.validation.IncreaseIndexRequest;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTemplateRequest(
        @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
        @NotNull(message = "템플릿 이름이 null 입니다.")
        @Size(max = 255, message = "템플릿 이름은 최대 255자까지 입력 가능합니다.")
        String title,
        @Schema(description = "템플릿의 스니펫 내역")
        @NotNull(message = "스니펫 리스트가 null 입니다.")
        @Valid
        List<CreateSnippetRequest> snippets

) implements IncreaseIndexRequest {
    @Override
    public List<Integer> increasedIndexes() {
        return snippets.stream().map(CreateSnippetRequest::ordinal).toList();
    }
}
