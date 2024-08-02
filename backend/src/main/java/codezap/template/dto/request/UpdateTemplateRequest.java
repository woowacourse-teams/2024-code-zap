package codezap.template.dto.request;

import java.util.List;
import java.util.stream.Stream;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import codezap.global.validation.ByteLength;
import codezap.template.dto.request.validation.ValidatedSnippetsOrdinalRequest;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateTemplateRequest(
        @Schema(description = "템플릿 이름", example = "스프링 로그인 구현")
        @NotNull(message = "템플릿 이름이 null 입니다.")
        @Size(max = 255, message = "템플릿 이름은 최대 255자까지 입력 가능합니다.")
        String title,

        @Schema(description = "템플릿 설명", example = "JWT를 사용하여 로그인 기능을 구현함")
        @NotNull(message = "템플릿 설명이 null 입니다.")
        @ByteLength(max = 65_535, message = "템플릿 설명은 최대 65,535 Byte까지 입력 가능합니다.")
        String description,

        @Schema(description = "새로 추가한 스니펫 내역")
        @NotNull(message = "createSnippets 리스트가 null 입니다.")
        @Valid
        List<CreateSnippetRequest> createSnippets,

        @Schema(description = "삭제, 생성 스니펫을 제외한 모든 스니펫 내역")
        @NotNull(message = "updateSnippets 리스트가 null 입니다.")
        @Valid
        List<UpdateSnippetRequest> updateSnippets,

        @Schema(description = "삭제한 스니펫 식별자")
        @NotNull(message = "deleteSnippetIds 리스트가 null 입니다.")
        List<Long> deleteSnippetIds,

        @Schema(description = "카테고리 ID", example = "1")
        @NotNull(message = "카테고리 id가 null 입니다.")
        Long categoryId,

        @Schema(description = "태그 리스트")
        @NotNull(message = "태그 리스트가 null 입니다.")
        List<String> tags
) implements ValidatedSnippetsOrdinalRequest {
    @Override
    public List<Integer> extractSnippetsOrdinal() {
        return Stream.concat(
                updateSnippets.stream().map(UpdateSnippetRequest::ordinal),
                createSnippets.stream().map(CreateSnippetRequest::ordinal)
        ).toList();
    }
}
