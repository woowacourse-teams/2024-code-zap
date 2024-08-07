package codezap.template.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.member.dto.MemberDto;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.ExploreTemplatesResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "템플릿 CRUD API", description = "템플릿 생성, 단건 및 목록 조회, 삭제, 수정 API")
public interface SpringDocTemplateController {

    @Operation(summary = "템플릿 생성", description = """
            새로운 템플릿을 생성합니다. \n
            템플릿의 제목, 썸네일 스니펫의 순서, 스니펫 목록, 카테고리 ID, 태그 목록이 필요합니다. \n
            스니펫 목록은 파일 이름, 소스 코드, 해당 스니펫의 순서가 필요합니다. \n
            * 썸네일 스니펫은 1로 고정입니다. (2024.07.15 기준) \n
            * 모든 스니펫 순서는 1부터 시작합니다. \n
            * 스니펫 순서는 오름차순으로 정렬하여 보내야 합니다. \n
            """)
    @ApiResponse(responseCode = "201", description = "템플릿 생성 성공", headers = {
            @Header(name = "생성된 템플릿의 API 경로", example = "/templates/1")})
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/templates", errorCases = {
            @ErrorCase(description = "모든 필드 중 null인 값이 있는 경우", exampleMessage = "템플릿 이름 null 입니다."),
            @ErrorCase(description = "제목 또는 스니펫 파일 또는 태그 이름이 255자를 초과한 경우", exampleMessage = "제목은 최대 255자까지 입력 가능합니다."),
            @ErrorCase(description = "썸네일 스니펫의 순서가 1이 아닌 경우", exampleMessage = "썸네일 스니펫의 순서가 잘못되었습니다."),
            @ErrorCase(description = "스니펫 순서가 잘못된 경우", exampleMessage = "스니펫 순서가 잘못되었습니다."),
            @ErrorCase(description = "스니펫 내용 65,535 byte를 초과한 경우", exampleMessage = "파일 내용은 최대 65,535 byte까지 입력 가능합니다.")
    })
    ResponseEntity<Void> create(CreateTemplateRequest createTemplateRequest, MemberDto memberDto);

    @Operation(summary = "템플릿 검색", description = """
            필터링 조건에 맞는 모든 템플릿을 조회합니다.
            - 필터링 조건
              - 멤버 ID
              - 검색 키워드 (템플릿 제목, 템플릿 설명, 스니펫 파일명, 소스 코드)
              - 카테고리 ID
              - 태그 ID
            - 정렬 방식
              - 최신순 (createdAt,asc)
              - 오래된순 (createdAt,desc)
            조회 조건으로 페이지 인덱스, 한 페이지에 들어갈 최대 템플릿의 개수를 변경할 수 있습니다.
            페이지 인덱스는 1, 템플릿 개수는 20개가 기본 값입니다.
            """)
    @ApiResponse(responseCode = "200", description = "템플릿 단건 조회 성공",
            content = {@Content(schema = @Schema(implementation = ExploreTemplatesResponse.class))})
    ResponseEntity<FindAllTemplatesResponse> getTemplates(
            MemberDto memberDto,
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    );

    @Operation(summary = "템플릿 단건 조회", description = "해당하는 식별자의 템플릿을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "템플릿 단건 조회 성공",
            content = {@Content(schema = @Schema(implementation = ExploreTemplatesResponse.class))})
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "해당하는 id 값인 템플릿이 없는 경우", exampleMessage = "식별자 1에 해당하는 템플릿이 존재하지 않습니다."),
    })
    ResponseEntity<FindTemplateResponse> getTemplateById(Long id, MemberDto memberDto);

    @Operation(summary = "템플릿 수정", description = "해당하는 식별자의 템플릿을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "템플릿 수정 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "해당하는 id 값인 템플릿이 없는 경우", exampleMessage = "식별자 1에 해당하는 템플릿이 존재하지 않습니다."),
    })
    ResponseEntity<Void> updateTemplate(Long id, UpdateTemplateRequest updateTemplateRequest, MemberDto memberDto);

    @Operation(summary = "템플릿 삭제", description = "해당하는 식별자의 템플릿을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "템플릿 삭제 성공")
    ResponseEntity<Void> deleteTemplate(Long id, MemberDto memberDto);
}
