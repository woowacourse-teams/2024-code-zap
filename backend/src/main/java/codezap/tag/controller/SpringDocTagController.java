package codezap.tag.controller;

import org.springframework.http.ResponseEntity;

import codezap.member.dto.MemberDto;
import codezap.template.dto.response.FindAllTagsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "태그 API", description = "템플릿 조회 API")
public interface SpringDocTagController {
    @Operation(summary = "태그 목록 조회", description = """
            유저가 가지고 있는 태그 목록을 조회합니다.
            """)
    @ApiResponse(responseCode = "200", description = "태그 목록 조회 성공",
            content = {@Content(schema = @Schema(implementation = FindAllTagsResponse.class))})
    ResponseEntity<FindAllTagsResponse> getTags(MemberDto memberDto, Long memberId);

}
