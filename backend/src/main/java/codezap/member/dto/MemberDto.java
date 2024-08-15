package codezap.member.dto;

import codezap.member.domain.Member;

public record MemberDto(
        Long id,
        String loginId,
        String password
) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getLoginId(), member.getPassword());
    }
}
