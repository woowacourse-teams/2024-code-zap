package codezap.member.dto.response;

import codezap.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindMemberResponse(
        @Schema(description = "아이디", example = "code")
        String loginId
) {
    public static FindMemberResponse from(Member member) {
        return new FindMemberResponse(member.getLoginId());
    }
}
