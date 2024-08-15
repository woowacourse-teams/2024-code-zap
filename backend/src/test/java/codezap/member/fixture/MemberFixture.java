package codezap.member.fixture;

import codezap.member.domain.Member;

public class MemberFixture {

    public static Member memberFixture() {
        return new Member(
                1L,
                "몰리",
                "password1234"
        );
    }

    public static Member createFixture(String loginId) {
        return new Member(
                "몰리",
                "password1234"
        );
    }
}
