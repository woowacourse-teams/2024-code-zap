package codezap.template.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;

class TemplateTagTest {

    @Nested
    @DisplayName("TemplateTag 이 Template 을 가지고 있는지 조회")
    class HasTemplate {

        @Test
        @DisplayName("TemplateTag 이 Template 을 가지고 있는지 조회: 참")
        void hasTemplate() {
            Template template = createTemplateById(1L);
            TemplateTag templateTag = new TemplateTag(template, new Tag(1L, "tag1"));

            assertTrue(templateTag.hasTemplate(template));
        }

        @Test
        @DisplayName("TemplateTag 이 Template 을 가지고 있는지 조회: 거짓")
        void hasNotTemplate() {
            Template template = createTemplateById(1L);
            Template otherTemplate = createTemplateById(2L);
            TemplateTag templateTag = new TemplateTag(template, new Tag(1L, "tag1"));

            assertFalse(templateTag.hasTemplate(otherTemplate));
        }

        private Template createTemplateById(Long id) {
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getFirstCategory();
            List<SourceCode> sourceCodes = List.of();
            long likesCount = 1L;
            return new Template(
                    id, member, "Template 1", "Description 1", category, sourceCodes, likesCount, Visibility.PUBLIC);
        }
    }
}
