package codezap.template.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;

class TemplateTest {

    @Nested
    @DisplayName("권한 확인")
    class ValidateAuthorization {

        @Test
        @DisplayName("성공")
        void validateAuthorizationSuccess() {
            Member member = MemberFixture.getFirstMember();
            Template template = TemplateFixture.get(member, Category.createDefaultCategory(member));

            assertThatCode(() -> template.validateAuthorization(member))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("성공: 다른 사용자일 경우 예외 발생")
        void validateAuthorizationFail() {
            Member member = MemberFixture.getFirstMember();
            Member otherMember = MemberFixture.getSecondMember();
            Template template = TemplateFixture.get(member, Category.createDefaultCategory(member));

            assertThatThrownBy(() -> template.validateAuthorization(otherMember))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿에 대한 권한이 없습니다.")
                    .extracting("errorCode").isEqualTo(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

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
    @DisplayName("Private 템플릿 확인")
    class ValidateForbiddenPrivate {

        @Test
        @DisplayName("성공")
        void validateForbiddenPrivateSuccess() {
            Member member = MemberFixture.getFirstMember();
            Template template = TemplateFixture.get(member, Category.createDefaultCategory(member));

            assertThatCode(template::validateForbiddenPrivate)
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("성공: private 템플릿일 경우 예외 발생")
        void validateForbiddenPrivateFail() {
            Member member = MemberFixture.getFirstMember();
            Template template = TemplateFixture.getPrivate(member, Category.createDefaultCategory(member));

            assertThatThrownBy(template::validateForbiddenPrivate)
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿은 비공개 템플릿입니다.")
                    .extracting("errorCode").isEqualTo(ErrorCode.FORBIDDEN_ACCESS);
        }
    }
}
