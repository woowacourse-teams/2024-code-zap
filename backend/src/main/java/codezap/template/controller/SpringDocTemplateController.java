package codezap.template.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateByIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "템플릿 CRUD API", description = "템플릿 생성, 단건 및 목록 조회, 삭제, 수정 API")
public interface SpringDocTemplateController {

    @Operation(summary = "템플릿 생성", description = """
            새로운 템플릿을 생성합니다. \n
            템플릿의 제목, 썸네일 스니펫의 순서, 스니펫 목록이 필요합니다. \n
            스니펫 목록은 파일 이름, 소스 코드, 해당 스니펫의 순서가 필요합니다. \n
            * 썸네일 스니펫은 1로 고정입니다. (2024.07.15 기준) \n
            * 모든 스니펫 순서는 1부터 시작합니다. \n
            * 스니펫 순서는 오름차 순으로 정렬하여 보내야 합니다. \n
            """)
    @ApiResponse(responseCode = "201", description = "회원 예약 생성 성공", headers = {
            @Header(name = "생성된 템플릿의 API 경로", example = "/templates/1")})
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ProblemDetail.class),
            examples = {
                    @ExampleObject(summary = "모든 필드 중 null인 값이 있는 경우",
                            name = "메시지 예시: 템플릿 이름 null 입니다."),
                    @ExampleObject(summary = "제목 또는 스니펫 파일명이 255자를 초과한 경우",
                            name = "메시지 예시: 제목은 최대 255자까지 입력 가능합니다."),
                    @ExampleObject(summary = "썸네일 스니펫의 순서가 1이 아닌 경우",
                            name = "메시지 예시: 썸네일 스니펫의 순서가 잘못되었습니다."),
                    @ExampleObject(summary = "스니펫 순서가 잘못된 경우 (ex. 1 -> 3 -> 2 순으로 스니펫 목록이 온 경우)",
                            name = "메시지 예시: 스니펫 순서가 잘못되었습니다."),
                    @ExampleObject(summary = "스니펫 내용 65,535 byte를 초과한 경우",
                            name = "메시지 예시: 파일 내용은 최대 65,535 byte까지 입력 가능합니다.")}))
    ResponseEntity<Void> create(CreateTemplateRequest createTemplateRequest);

    @Operation(summary = "템플릿 목록 조회", description = "작성된 모든 템플릿을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = {@Content(schema = @Schema(implementation = FindAllTemplatesResponse.class))})
    ResponseEntity<FindAllTemplatesResponse> getTemplates();

    @Operation(summary = "템플릿 단건 조회", description = "해당하는 식별자의 템플릿을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "템플릿 단건 조회 성공",
            content = {@Content(schema = @Schema(implementation = FindAllTemplatesResponse.class))})
    @ApiResponse(responseCode = "400", description = "해당하는 id 값인 템플릿이 없는 경우",
            content = {@Content(schema = @Schema(implementation = ProblemDetail.class))})
    ResponseEntity<FindTemplateByIdResponse> getTemplateById(Long id);

    @Operation(summary = "템플릿 수정", description = "해당하는 식별자의 템플릿을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "템플릿 수정 성공")
    @ApiResponse(responseCode = "400", description = "해당하는 id 값인 템플릿이 없는 경우",
            content = {@Content(schema = @Schema(implementation = ProblemDetail.class))})
    ResponseEntity<Void> updateTemplate(Long id, UpdateTemplateRequest updateTemplateRequest);

    @Operation(summary = "템플릿 삭제", description = "해당하는 식별자의 템플릿을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "템플릿 삭제 성공")
    @ApiResponse(responseCode = "400", description = "해당하는 id 값인 템플릿이 없는 경우",
            content = {@Content(schema = @Schema(implementation = ProblemDetail.class))})
    ResponseEntity<Void> deleteTemplate(Long id);
}
