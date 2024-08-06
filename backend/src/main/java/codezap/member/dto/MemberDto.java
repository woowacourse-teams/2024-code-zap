package codezap.member.dto;

import codezap.member.domain.Member;

public record MemberDto(
        Long id,
        String email,
        String password,
        String username
) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getPassword(), member.getUsername());
    }
}
