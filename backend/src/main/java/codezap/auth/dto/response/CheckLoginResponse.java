package codezap.auth.dto.response;

import codezap.member.domain.Member;

public record CheckLoginResponse(long id, String name) {
    public CheckLoginResponse(Member member) {
        this(member.getId(), member.getName());
    }
}
