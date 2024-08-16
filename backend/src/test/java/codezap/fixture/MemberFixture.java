package codezap.fixture;

import codezap.member.domain.Member;

public class MemberFixture {
    public static Member getFirstMember() {
        return new Member(1L, "test1@email.com", "password1234", "username1");
    }

    public static Member getSecondMember() {
        return new Member(2L, "test2@email.com", "password1234", "username2");
    }
}
