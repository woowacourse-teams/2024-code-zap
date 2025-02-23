package codezap.category.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.member.domain.Member;

public class CategoryTest {

    @Nested
    @DisplayName("템플릿 개수 증가 성공")
    class IncreaseTemplateCount {

        @Test
        @DisplayName("템플릿 개수 증가 성공")
        void increaseTemplateCountSuccess() {
            Category category = CategoryFixture.getDefaultCategory(MemberFixture.getFirstMember());

            category.increaseTemplateCount();

            assertThat(category.getTemplateCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("템플릿 개수 감소 성공")
    class DecreaseTemplateCount {

        @Test
        @DisplayName("템플릿 개수 감소 성공")
        void decreaseTemplateCountSuccess() {
            Category category = CategoryFixture.getDefaultCategory(MemberFixture.getFirstMember());

            category.increaseTemplateCount();
            category.increaseTemplateCount();
            category.decreaseTemplateCount();

            assertThat(category.getTemplateCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("템플릿 개수 감소 성공: 이미 0개인 경우에도 성공")
        void decreaseTemplateCountSuccessAlreadyZero() {
            Category category = CategoryFixture.getDefaultCategory(MemberFixture.getFirstMember());

            category.decreaseTemplateCount();

            assertThat(category.getTemplateCount()).isEqualTo(0);
        }
    }
}
