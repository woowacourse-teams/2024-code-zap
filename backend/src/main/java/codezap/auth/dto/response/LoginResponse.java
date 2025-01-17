package codezap.auth.dto.response;

import codezap.auth.dto.LoginMember;

public record LoginResponse(
        long memberId,
        String name
) {
    public static LoginResponse from(LoginMember loginMember) {
        return new LoginResponse(loginMember.id(), loginMember.name());
    }
}
