package codezap.likes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.member.dto.MemberDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "좋아요 API", description = "좋아요 기능 관련 API")
public interface SpringDocsLikesController {

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "좋아요", description = "템플릿을 좋아요 합니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/like/1", errorCases = {
            @ErrorCase(description = "템플릿 Id 에 해당하는 템플릿이 존재하지 않는 경우", exampleMessage = "식별자 1에 해당하는 템플릿이 존재하지 않습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/like/1", errorCases = {
            @ErrorCase(description = "인증 정보에 해당하는 멤버가 없는 경우", exampleMessage = "인증 정보가 정확하지 않습니다.")
    })
    ResponseEntity<Void> like(MemberDto memberDto, long templateId);

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "좋아요 취소", description = "템플릿 좋아요를 취소합니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 취소 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/dislike/1", errorCases = {
            @ErrorCase(description = "템플릿 Id 에 해당하는 템플릿이 존재하지 않는 경우", exampleMessage = "식별자 1에 해당하는 템플릿이 존재하지 않습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/dislike/1", errorCases = {
            @ErrorCase(description = "인증 정보에 해당하는 멤버가 없는 경우", exampleMessage = "인증 정보가 정확하지 않습니다.")
    })
    ResponseEntity<Void> cancelLike(MemberDto memberDto, long templateId);
}
