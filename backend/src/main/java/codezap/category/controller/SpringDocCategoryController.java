package codezap.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "카테고리 CRUD API", description = "카테고리 생성, 목록 조회, 삭제, 수정 API")
public interface SpringDocCategoryController {

    @Operation(summary = "카테고리 생성", description = """
            새로운 카테고리를 생성합니다. \n
            새로운 카테고리의 이름이 필요합니다. \n
            """)
    @ApiResponse(responseCode = "201", description = "카테고리 생성 성공", headers = {
            @Header(name = "생성된 카테고리의 API 경로", example = "/categories/1")})
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/categories", errorCases = {
            @ErrorCase(description = "모든 필드 중 null인 값이 있는 경우", exampleMessage = "카테고리 이름이 null 입니다."),
            @ErrorCase(description = "카테고리 이름이 255자를 초과한 경우", exampleMessage = "카테고리 이름은 최대 255자까지 입력 가능합니다.")
    })
    ResponseEntity<Void> createCategory(CreateCategoryRequest createCategoryRequest);

    @Operation(summary = "카테고리 목록 조회", description = "생성된 모든 카테고리를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = {@Content(schema = @Schema(implementation = FindAllCategoriesResponse.class))})
    ResponseEntity<FindAllCategoriesResponse> getCategories();

    @Operation(summary = "카테고리 수정", description = "해당하는 식별자의 카테고리를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/categories/1", errorCases = {
            @ErrorCase(description = "해당하는 id 값인 카테고리가 없는 경우",
                    exampleMessage = "식별자 1에 해당하는 카테고리가 존재하지 않습니다."),
    })
    ResponseEntity<Void> updateCategory(Long id, UpdateCategoryRequest updateCategoryRequest);

    @Operation(summary = "카테고리 삭제", description = "해당하는 식별자의 카테고리를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공")
    ResponseEntity<Void> deleteCategory(Long id);
}
