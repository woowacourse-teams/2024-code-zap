package codezap.template.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.member.domain.Member;

class TemplateTest {

    @Nested
    @DisplayName("멤버 확인")
    class MatchMember {

        @Test
        @DisplayName("성공: 같은 사용자일 경우 true")
        void matchMemberSuccess() {
            Member member = MemberFixture.getFirstMember();
            Template template = TemplateFixture.get(member, Category.createDefaultCategory(member));

            boolean actual = template.matchMember(member);

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("성공: 다른 사용자일 경우 false")
        void matchMemberFail() {
            Member member = MemberFixture.getFirstMember();
            Member otherMember = MemberFixture.getSecondMember();
            Template template = TemplateFixture.get(member, Category.createDefaultCategory(member));

            boolean actual = template.matchMember(otherMember);

            assertThat(actual).isFalse();
        }
    }

    @Nested
    @DisplayName("공개 범위 확인")
    class IsPrivate {

        @Test
        @DisplayName("성공: 비공개 템플릿일 경우 true")
        void isPrivateTrue() {
            Member member = MemberFixture.getFirstMember();
            Template template = TemplateFixture.getPrivate(member, Category.createDefaultCategory(member));

            boolean actual = template.isPrivate();

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("성공: 공개 템플릿일 경우 false")
        void isPrivateFalse() {
            Member member = MemberFixture.getFirstMember();
            Member otherMember = MemberFixture.getSecondMember();
            Template template = TemplateFixture.get(member, Category.createDefaultCategory(member));

            boolean actual = template.isPrivate();

            assertThat(actual).isFalse();
        }
    }

    @Test
    @DisplayName("좋아요 갱신 성공")
    void updateLike() {
        Member member = MemberFixture.getFirstMember();
        Template template = TemplateFixture.get(member, Category.createDefaultCategory(member));

        template.updateLike();

        assertThat(template.getLikesCount()).isEqualTo(1L);
    }

    @Nested
    @DisplayName("좋아요 취소")
    class CancelLike {

        @Test
        @DisplayName("좋아요 취소 성공")
        void cancelLike() {
            Member member = MemberFixture.getFirstMember();
            Template template = TemplateFixture.get(member, Category.createDefaultCategory(member));
            template.updateLike();
            template.updateLike();

            template.cancelLike();

            assertThat(template.getLikesCount()).isEqualTo(1L);
        }
        @Test
        @DisplayName("좋아요 취소 실패: 이미 count가 0인 경우")
        void cancelLikeWhenLikeAlreadyFail() {
            Member member = MemberFixture.getFirstMember();
            Template template = TemplateFixture.get(member, Category.createDefaultCategory(member));

            assertThatThrownBy(() -> template.cancelLike())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("좋아요가 0보다 작을 수 없습니다.");
        }

    }
}
