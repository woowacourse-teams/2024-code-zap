package codezap.auth.dto;

import codezap.member.domain.Member;

public record LoginMember (
        long id,
        String name,
        String password,
        String salt
){
    public static LoginMember from(Member member) {
        return new LoginMember(member.getId(), member.getName(), member.getPassword(), member.getSalt());
    }
}
