package codezap.fixture;

import codezap.member.dto.MemberDto;

public class MemberDtoFixture {

    public static MemberDto getFirstMemberDto() {
        return new MemberDto(
                1L,
                "몰리",
                "password1234"
        );
    }

    public static MemberDto getSecondMemberDto() {
        return new MemberDto(
                2L,
                "몰리2",
                "password1234"
        );
    }
}
