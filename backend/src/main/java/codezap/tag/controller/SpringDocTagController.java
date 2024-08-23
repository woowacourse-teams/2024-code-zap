package codezap.tag.controller;

import org.springframework.http.ResponseEntity;

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
    ResponseEntity<FindAllTagsResponse> getTags(Long memberId);
}
