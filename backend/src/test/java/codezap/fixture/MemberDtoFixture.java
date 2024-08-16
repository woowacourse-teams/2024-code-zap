package codezap.fixture;

import codezap.member.dto.MemberDto;

public class MemberDtoFixture {

    public static MemberDto getFirstMemberDto() {
        return new MemberDto(1L,  "name1", "password1234");
    }

    public static MemberDto getSecondMemberDto() {
        return new MemberDto(2L, "name2", "password1234");
    }
}
