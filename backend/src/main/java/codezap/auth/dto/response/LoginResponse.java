package codezap.auth.dto.response;

import codezap.member.domain.Member;

public record LoginResponse(
        Long memberId,
        String name
) {
    public static LoginResponse from(Member member) {
        return new LoginResponse(member.getId(), member.getName());
    }
}
