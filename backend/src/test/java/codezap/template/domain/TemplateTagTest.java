package codezap.template.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;

class TemplateTagTest {

    @Nested
    @DisplayName("TemplateTag 이 Template 을 가지고 있는지 조회")
    class HasTemplate {

        @Test
        @DisplayName("TemplateTag 이 Template 을 가지고 있는지 조회: 참")
        void hasTemplate() {
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getDefaultCategory(member);

            Template template = TemplateFixture.get(member, category);
            TemplateTag templateTag = new TemplateTag(template, new Tag(1L, "tag1"));

            assertTrue(templateTag.hasTemplate(template));
        }

        @Test
        @DisplayName("TemplateTag가 Template 을 가지고 있는지 조회: 거짓")
        void hasNotTemplate() {
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getDefaultCategory(member);

            Template template = TemplateFixture.get(member, category);
            Template otherTemplate = TemplateFixture.getPrivate(member, category);
            TemplateTag templateTag = new TemplateTag(template, new Tag(1L, "tag1"));

            assertFalse(templateTag.hasTemplate(otherTemplate));
        }
    }
}
