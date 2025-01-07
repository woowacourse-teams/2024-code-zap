package codezap.member.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.fixture.MemberFixture;

class MemberTest {

    @Nested
    @DisplayName("비밀번호 확인")
    class MatchPassword {

        @Test
        @DisplayName("성공: 비밀번호 일치 true 반환")
        void matchPassword() {
            Member member = MemberFixture.getFirstMember();
            assertTrue(member.matchPassword(member.getPassword()));
        }

        @Test
        @DisplayName("성공: 비밀번호 불일치 false 반환")
        void matchPassword_Fail_NotSamePassword() {
            Member member = MemberFixture.getFirstMember();
            assertFalse(member.matchPassword(member.getPassword() + "wrong"));
        }
    }

    @Nested
    @DisplayName("아이디 확인")
    class MatchId {

        @Test
        @DisplayName("성공: 아이디 일치 true 반환")
        void matchId() {
            Member member = MemberFixture.getFirstMember();
            assertTrue(member.matchId(member.getId()));
        }

        @Test
        @DisplayName("성공: 아이디 불일치 false 반환")
        void matchId_Fail_NotSamePassword() {
            Member member = MemberFixture.getFirstMember();
            assertFalse(member.matchId(100L));
        }
    }

    @Test
    void matchId() {
    }
}
