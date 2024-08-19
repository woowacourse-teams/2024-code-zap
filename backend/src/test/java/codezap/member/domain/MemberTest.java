package codezap.member.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import codezap.member.fixture.MemberFixture;

class MemberTest {

    @Test
    @DisplayName("비밀번호 일치")
    void matchPassword() {
        Member member = MemberFixture.memberFixture();
        assertTrue(member.matchPassword(member.getPassword()));
    }

    @Test
    @DisplayName("비밀번호 불일치")
    void matchPassword_Fail_NotSamePassword() {
        Member member = MemberFixture.memberFixture();
        assertFalse(member.matchPassword(member.getPassword() + "wrong"));
    }
}
