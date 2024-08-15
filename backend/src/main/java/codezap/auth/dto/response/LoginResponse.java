package codezap.auth.dto.response;

import codezap.member.domain.Member;

public record LoginResponse(
        long memberId,
        String username
) {
    public static LoginResponse from(Member member) {
        return new LoginResponse(member.getId(), member.getLoginId());
    }
}
