package codezap.fixture;

import codezap.member.dto.MemberDto;

public class MemberDtoFixture {

    public static MemberDto getFirstMemberDto() {
        return new MemberDto(1L,  "username1", "password1234");
    }

    public static MemberDto getSecondMemberDto() {
        return new MemberDto(2L, "username2", "password1234");
    }
}
