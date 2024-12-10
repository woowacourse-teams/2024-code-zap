package codezap.auth.dto;

import codezap.auth.dto.response.LoginResponse;
import codezap.member.domain.Member;

public record LoginAndMemberDto(
        LoginResponse loginResponse,
        Member member
) {
}
