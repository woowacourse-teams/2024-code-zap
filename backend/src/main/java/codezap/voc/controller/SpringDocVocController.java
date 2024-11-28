package codezap.voc.controller;

import org.springframework.http.HttpStatus;

import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.voc.dto.VocRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "문의하기")
public interface SpringDocVocController {

    @Operation(summary = "문의하기")
    @ApiResponse(responseCode = "201", description = "VOC 스프레드시트 데이터 추가 성공")
    @ApiErrorResponse(
            status = HttpStatus.BAD_REQUEST,
            instance = "/contact",
            errorCases = {
                    @ErrorCase(description = "문의 내용을 입력하지 않은 경우.", exampleMessage = "문의 내용을 입력해주세요."),
                    @ErrorCase(description = "문의 내용이 20자 미만인 경우.", exampleMessage = "문의 내용을 20자 이상 입력해주세요."),
                    @ErrorCase(description = "문의 내용이 10,000 글자를 초과한 경우.", exampleMessage = "문의 내용은 최대 10,000 글자까지 입력할 수 있습니다.")
            }
    )
    void create(VocRequest request);
}
