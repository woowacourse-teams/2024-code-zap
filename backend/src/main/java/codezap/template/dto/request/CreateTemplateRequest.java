package codezap.template.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import codezap.global.validation.ByteLength;
import codezap.global.validation.ValidationGroups.ByteLengthGroup;
import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;
import codezap.template.dto.request.validation.ValidatedSnippetsOrdinalRequest;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTemplateRequest(
        @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
        @NotBlank(message = "템플릿 이름이 null 입니다.", groups = NotNullGroup.class)
        @Size(max = 255, message = "템플릿 이름은 최대 255자까지 입력 가능합니다.", groups = SizeCheckGroup.class)
        String title,

        @Schema(description = "템플릿 설명", example = "JWT를 사용하여 로그인 기능을 구현함")
        @NotNull(message = "템플릿 설명이 null 입니다.", groups = NotNullGroup.class)
        @ByteLength(max = 65_535, message = "템플릿 설명은 최대 65,535 Byte까지 입력 가능합니다.", groups = ByteLengthGroup.class)
        String description,

        @Schema(description = "템플릿의 스니펫 내역")
        @NotNull(message = "스니펫 리스트가 null 입니다.", groups = NotNullGroup.class)
        @Size(min = 1, message = "스니펫은 최소 1개 입력해야 합니다.", groups = SizeCheckGroup.class)
        @Valid
        List<CreateSnippetRequest> snippets,

        @Schema(description = "카테고리 ID", example = "1")
        @NotNull(message = "카테고리 id가 null 입니다.")
        Long categoryId,

        @Schema(description = "태그 리스트")
        @NotNull(message = "태그 리스트가 null 입니다.")
        List<String> tags
) implements ValidatedSnippetsOrdinalRequest {
    @Override
    public List<Integer> extractSnippetsOrdinal() {
        return snippets.stream().map(CreateSnippetRequest::ordinal).toList();
    }
}
