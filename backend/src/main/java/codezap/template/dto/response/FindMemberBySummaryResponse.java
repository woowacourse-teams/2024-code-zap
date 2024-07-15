package codezap.template.dto.response;

import codezap.member.domain.Member;

public record FindMemberBySummaryResponse(
        Long id,
        String nickname
) {
    public static FindMemberBySummaryResponse from(Member member) {
        return new FindMemberBySummaryResponse(
                member.getId(),
                member.getNickname()
        );
    }
}
