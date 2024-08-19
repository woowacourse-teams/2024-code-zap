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

    public static Member createFixture(String name) {
        return new Member(
                name,
                "password1234"
        );
    }
}
