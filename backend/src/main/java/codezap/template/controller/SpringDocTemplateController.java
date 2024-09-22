package codezap.template.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.member.domain.Member;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "템플릿 CRUD API", description = "템플릿 생성, 단건 및 목록 조회, 삭제, 수정 API")
public interface SpringDocTemplateController {

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "템플릿 생성", description = """
            새로운 템플릿을 생성합니다. \n
            템플릿명, 템플릿 설명, 소스 코드 목록, 썸네일 순서, 카테고리 ID, 태그 목록이 필요합니다. \n
            * 템플릿 이름은 비어있거나 공백일 수 없다.
            
            소스 코드 목록은 파일명, 소스 코드, 소스 코드 순서가 필요합니다. \n
            * 소스 코드 순서는 1부터 시작합니다.
            * 소스 코드 순서는 오름차순으로 정렬하여 보내야 합니다.
            """)
    @ApiResponse(responseCode = "201", description = "템플릿 생성 성공", headers = {
            @Header(name = "생성된 템플릿의 API 경로", example = "/templates/1")})
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/templates", errorCases = {
            @ErrorCase(description = "모든 필드 중 null인 값이 있는 경우", exampleMessage = "템플릿 설명이 null 입니다."),
            @ErrorCase(description = "템플릿명, 파일명, 소스 코드가 공백일 경우", exampleMessage = "템플릿명이 비어 있거나 공백입니다."),
            @ErrorCase(description = "템플릿명, 파일명이 255자를 초과한 경우", exampleMessage = "템플릿명은 최대 255자까지 입력 가능합니다."),
            @ErrorCase(description = "태그명이 30자를 초과한 경우", exampleMessage = "태그명은 최대 30자까지 입력 가능합니다."),
            @ErrorCase(description = "소스 코드가 65,535 byte를 초과한 경우",
                    exampleMessage = "소스 코드는 최대 65,535 Byte까지 입력 가능합니다."),
            @ErrorCase(description = "소스 코드 순서가 잘못된 경우", exampleMessage = "소스 코드 순서가 잘못되었습니다."),
            @ErrorCase(description = "소스 코드가 0개 입력된 경우", exampleMessage = "소스 코드는 최소 1개 입력 해야 합니다."),
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "카테고리 권한이 없는 경우", exampleMessage = "해당 카테고리에 대한 권한이 없습니다."),
    })
    @ApiErrorResponse(status = HttpStatus.NOT_FOUND, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "인증 정보에 포함된 멤버가 없는 경우", exampleMessage = "식별자 1에 해당하는 멤버가 존재하지 않습니다."),
            @ErrorCase(description = "카테고리가 없는 경우", exampleMessage = "식별자 1에 해당하는 카테고리가 존재하지 않습니다."),
            @ErrorCase(description = "이름에 맞는 태그가 없는 경우", exampleMessage = "이름이 tag1인 태그는 존재하지 않습니다."),
            @ErrorCase(description = "해당 순서인 소스 코드가 없는 경우", exampleMessage = "템플릿에 1번째 소스 코드가 존재하지 않습니다."),
    })
    ResponseEntity<Void> createTemplate(Member member, CreateTemplateRequest createTemplateRequest);

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "템플릿 조회", description = """
            조건에 맞는 모든 템플릿을 조회합니다. \n
            - 조건 \n
              - 멤버 ID
              - 검색 키워드 (템플릿명, 템플릿 설명, 파일명, 소스 코드)
              - 카테고리 ID
              - 태그 ID들 \n
            
            페이징 조건을 줄 수 있습니다. 페이지 번호는 1, 템플릿 개수는 20, 정렬 방식은 최신순이 기본 값입니다. \n
            - 페이징 조건 \n
              - 페이지 번호(pageNumber)
              - 한 페이지에 템플릿 개수(pageSize)
              - 페이지 정렬 방식(sort) \n
            
            - 정렬 방식 \n
              - 최신순 (modifiedAt,asc)
              - 오래된순 (modifiedAt,desc) 
              - 좋아요 수가 많은 순 (like, desc) \n
            """)
    @ApiResponse(responseCode = "200", description = "템플릿 검색 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST,
            instance = "/templates?memberId=1&keyword=\"java\"&tagIds=", errorCases = {
            @ErrorCase(description = "태그 ID가 0개인 경우", exampleMessage = "태그 ID가 0개입니다. 필터링 하지 않을 경우 null로 전달해주세요."),
            @ErrorCase(description = "페이지 번호가 1보다 작을 경우", exampleMessage = "페이지 번호는 1 이상이어야 합니다."),
    })
    @ApiErrorResponse(status = HttpStatus.NOT_FOUND,
            instance = "/templates?memberId=1&keyword=\"java\"&categoryId=1&tagIds=1,2", errorCases = {
            @ErrorCase(description = "멤버가 없는 경우", exampleMessage = "식별자 1에 해당하는 멤버가 존재하지 않습니다."),
            @ErrorCase(description = "카테고리가 없는 경우", exampleMessage = "식별자 1에 해당하는 카테고리가 존재하지 않습니다."),
            @ErrorCase(description = "태그가 없는 경우", exampleMessage = "식별자 1에 해당하는 태그가 존재하지 않습니다."),
    })
    ResponseEntity<FindAllTemplatesResponse> findAllTemplates(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    );

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "템플릿 단건 조회", description = "해당하는 식별자의 템플릿을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "템플릿 단건 조회 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "해당하는 ID 값인 템플릿이 없는 경우", exampleMessage = "식별자 1에 해당하는 템플릿이 존재하지 않습니다."),
    })
    ResponseEntity<FindTemplateResponse> findTemplateById(Long id);

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "템플릿 수정", description = "해당하는 식별자의 템플릿을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "템플릿 수정 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "모든 필드 중 null인 값이 있는 경우", exampleMessage = "템플릿 설명이 null 입니다."),
            @ErrorCase(description = "템플릿명, 파일명, 소스 코드가 공백일 경우", exampleMessage = "템플릿명이 비어 있거나 공백입니다."),
            @ErrorCase(description = "템플릿명, 파일명, 태그명이 255자를 초과한 경우", exampleMessage = "템플릿명은 최대 255자까지 입력 가능합니다."),
            @ErrorCase(description = "태그명이 30자를 초과한 경우", exampleMessage = "태그명은 최대 30자까지 입력 가능합니다."),
            @ErrorCase(description = "소스 코드가 65,535 byte를 초과한 경우",
                    exampleMessage = "소스 코드는 최대 65,535 Byte까지 입력 가능합니다."),
            @ErrorCase(description = "소스 코드 순서가 잘못된 경우", exampleMessage = "소스 코드 순서가 잘못되었습니다."),
            @ErrorCase(description = "해당 템플릿의 실제 소스 코드 수와 인자로 받은 소스 코드 수가 다를 경우",
                    exampleMessage = "소스 코드의 정보가 정확하지 않습니다."),
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "자신의 템플릿이 아닐 경우", exampleMessage = "해당 템플릿에 대한 권한이 없습니다."),
            @ErrorCase(description = "카테고리 권한이 없는 경우", exampleMessage = "해당 카테고리에 대한 권한이 없습니다."),
    })
    @ApiErrorResponse(status = HttpStatus.NOT_FOUND, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "인증 정보에 포함된 멤버가 없는 경우", exampleMessage = "식별자 1에 해당하는 멤버가 존재하지 않습니다."),
            @ErrorCase(description = "카테고리가 없는 경우", exampleMessage = "식별자 1에 해당하는 카테고리가 존재하지 않습니다."),
            @ErrorCase(description = "태그가 없는 경우", exampleMessage = "식별자 1에 해당하는 태그가 존재하지 않습니다."),
            @ErrorCase(description = "소스 코드가 없는 경우", exampleMessage = "식별자 1에 해당하는 소스 코드가 존재하지 않습니다."),
    })
    ResponseEntity<Void> updateTemplate(Member member, Long id, UpdateTemplateRequest updateTemplateRequest);

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "템플릿 삭제", description = "해당하는 식별자의 템플릿들을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "템플릿 삭제 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/templates/1,1", errorCases = {
            @ErrorCase(description = "템플릿 ID가 중복된 경우", exampleMessage = "삭제하고자 하는 템플릿 ID가 중복되었습니다."),
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "자신의 템플릿이 아닐 경우", exampleMessage = "해당 템플릿에 대한 권한이 없습니다."),
    })
    @ApiErrorResponse(status = HttpStatus.NOT_FOUND, instance = "/templates/1", errorCases = {
            @ErrorCase(description = "인증 정보에 포함된 멤버가 없는 경우", exampleMessage = "식별자 1에 해당하는 멤버가 존재하지 않습니다."),
            @ErrorCase(description = "템플릿이 없는 경우", exampleMessage = "식별자 1에 해당하는 템플릿이 존재하지 않습니다."),
    })
    ResponseEntity<Void> deleteTemplates(Member member, List<Long> ids);
}
