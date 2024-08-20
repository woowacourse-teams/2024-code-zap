package codezap.member.dto;

import codezap.member.domain.Member;

public record MemberDto(
        Long id,
        String name,
        String password
) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getPassword());
    }
}
