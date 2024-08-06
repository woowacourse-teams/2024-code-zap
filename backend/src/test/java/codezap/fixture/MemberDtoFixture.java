package codezap.fixture;

import codezap.member.dto.MemberDto;

public class MemberDtoFixture {

    public static MemberDto getFirstMemberDto() {
        return new MemberDto(1L, "test1@email.com", "password1234", "username1");
    }

    public static MemberDto getSecondMemberDto() {
        return new MemberDto(2L, "test2@email.com", "password1234", "username2");
    }
}
