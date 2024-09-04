package codezap.fixture;

import codezap.member.domain.Member;

public class MemberFixture {
    public static Member getFirstMember() {
        return new Member(
                1L,
                "몰리",
                "password1234",
                "salt1"
        );
    }

    public static Member getSecondMember() {
        return new Member(
                2L,
                "몰리2",
                "password1234",
                "salt2"
        );
    }
}
