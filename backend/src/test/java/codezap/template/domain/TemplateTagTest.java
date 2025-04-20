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
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getDefaultCategory(member);

            Template template = new Template(1L, member, "안녕", "Description 1", category, 0L, Visibility.PUBLIC, 0L,
                    List.of(
                            new SourceCode("file1.java", "content1"),
                            new SourceCode("file2.java", "content2")
                    ));
            TemplateTag templateTag = new TemplateTag(template, new Tag(1L, "tag1"));

            assertTrue(templateTag.hasTemplate(template));
        }

        @Test
        @DisplayName("TemplateTag가 Template 을 가지고 있는지 조회: id가 다른 Template인 경우 거짓")
        void hasNotTemplate() {
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getDefaultCategory(member);

            Template template = new Template(1L, member, "안녕", "Description 1", category, 0L, Visibility.PUBLIC, 0L,
                    List.of(
                            new SourceCode("file1.java", "content1"),
                            new SourceCode("file2.java", "content2")
                    ));

            Template otherTemplate = new Template(2L, member, "안녕", "Description 1", category, 0L, Visibility.PUBLIC,
                    0L, List.of(new SourceCode("file1.java", "content1")));
            TemplateTag templateTag = new TemplateTag(template, new Tag(1L, "tag1"));

            assertFalse(templateTag.hasTemplate(otherTemplate));
        }
    }
}
