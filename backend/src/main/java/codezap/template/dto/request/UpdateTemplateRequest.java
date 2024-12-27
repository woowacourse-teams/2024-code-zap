package codezap.template.dto.request;

import java.util.List;
import java.util.stream.Stream;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import codezap.global.validation.ByteLength;
import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;
import codezap.template.domain.Visibility;
import codezap.template.dto.request.validation.ValidatedSourceCodesCountRequest;
import codezap.template.dto.request.validation.ValidatedOrdinalRequest;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateTemplateRequest(
        @Schema(description = "템플릿명", example = "스프링 로그인 구현")
        @NotBlank(message = "템플릿명이 비어 있거나 공백입니다.", groups = NotNullGroup.class)
        @Size(max = 255, message = "템플릿명은 최대 255자까지 입력 가능합니다.", groups = SizeCheckGroup.class)
        String title,

        @Schema(description = "템플릿 설명", example = "JWT를 사용하여 로그인 기능을 구현함")
        @NotNull(message = "템플릿 설명이 null 입니다.", groups = NotNullGroup.class)
        @ByteLength(max = 65_535, message = "템플릿 설명은 최대 65,535 Byte까지 입력 가능합니다.", groups = SizeCheckGroup.class)
        String description,

        @Schema(description = "추가하는 소스 코드 목록")
        @NotNull(message = "추가하는 소스 코드 목록이 null 입니다.", groups = NotNullGroup.class)
        @Valid
        List<CreateSourceCodeRequest> createSourceCodes,

        @Schema(description = "삭제, 생성 소스 코드를 제외한 모든 소스 코드 목록")
        @NotNull(message = "삭제, 생성 소스 코드를 제외한 모든 소스 코드 목록이 null 입니다.", groups = NotNullGroup.class)
        @Valid
        List<UpdateSourceCodeRequest> updateSourceCodes,

        @Schema(description = "삭제하는 소스 코드 ID 목록")
        @NotNull(message = "삭제하는 소스 코드 ID 목록이 null 입니다.", groups = NotNullGroup.class)
        List<Long> deleteSourceCodeIds,

        @Schema(description = "카테고리 ID", example = "1")
        @NotNull(message = "카테고리 ID가 null 입니다.", groups = NotNullGroup.class)
        Long categoryId,

        @Schema(description = "태그 목록")
        @NotNull(message = "태그 목록이 null 입니다.", groups = NotNullGroup.class)
        List<@Size(max = 30, message = "태그 명은 최대 30자까지 입력 가능합니다.", groups = SizeCheckGroup.class) String> tags,

        @Schema(description = "템플릿 공개 여부", example = "PUBLIC")
        @NotNull(message = "템플릿 공개 여부가 null 입니다.", groups = NotNullGroup.class)
        Visibility visibility
) implements ValidatedOrdinalRequest, ValidatedSourceCodesCountRequest {
    @Override
    public List<Integer> extractOrdinal() {
        return Stream.concat(
                updateSourceCodes.stream().map(UpdateSourceCodeRequest::ordinal),
                createSourceCodes.stream().map(CreateSourceCodeRequest::ordinal)
        ).toList();
    }

    @Override
    public Integer countSourceCodes() {
        return updateSourceCodes.size() + createSourceCodes.size();
    }
}
