package codezap.template.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTemplateRequest(
        @NotNull(message = "템플릿 이름이 null 입니다.")
        @Size(max = 255, message = "템플릿 이름은 최대 255자까지 입력 가능합니다.")
        String title,
        @NotNull(message = "스니펫 리스트가 null 입니다.")
        @Valid
        List<CreateSnippetRequest> snippets
) {
}
