package codezap.tag.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.member.dto.MemberDto;
import codezap.tag.dto.response.FindAllTagsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "태그 API", description = "태그 조회 API")
public interface SpringDocTagController {
    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "태그 조회", description = "해당 멤버의 템플릿들에 포함된 태그를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "태그 조회 성공")
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED,
            instance = "/tags/1", errorCases = {
            @ErrorCase(description = "인증 정보와 멤버 ID가 다른 경우", exampleMessage = "인증 정보에 포함된 멤버 ID와 파라미터로 받은 멤버 ID가 다릅니다."),
    })
    ResponseEntity<FindAllTagsResponse> getTags(MemberDto memberDto, Long memberId);
}
