package codezap.template.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.tag.domain.Tag;

class TemplateTagTest {

    @Nested
    @DisplayName("TemplateTag 이 Template 을 가지고 있는지 조회")
    class HasTemplate {

        @Test
        @DisplayName("TemplateTag 이 Template 을 가지고 있는지 조회: 참")
        void hasTemplate() {
            Template template = TemplateFixture.get(MemberFixture.getFirstMember(), CategoryFixture.getFirstCategory());
            TemplateTag templateTag = new TemplateTag(template, new Tag(1L, "tag1"));
            assertTrue(templateTag.hasTemplate(template));
        }

        @Test
        @DisplayName("TemplateTag 이 Template 을 가지고 있는지 조회: 거짓")
        void hasNotTemplate() {
            Template template = TemplateFixture.get(MemberFixture.getFirstMember(), CategoryFixture.getFirstCategory());
            Template otherTemplate = TemplateFixture.get(MemberFixture.getSecondMember(), CategoryFixture.getSecondCategory());

            TemplateTag templateTag = new TemplateTag(template, new Tag(1L, "tag1"));
            assertTrue(templateTag.hasTemplate(otherTemplate));
        }
    }
}
