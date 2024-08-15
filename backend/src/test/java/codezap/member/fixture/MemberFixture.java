package codezap.member.fixture;

import codezap.member.domain.Member;

public class MemberFixture {

    public static Member memberFixture() {
        return new Member(
                "code@zap.com",
                "password1234",
                "몰리"
        );
    }

    public static Member createMember(String email) {
        return new Member(
                1L,
                email,
                "password1234",
                "몰리"
        );
    }
}
