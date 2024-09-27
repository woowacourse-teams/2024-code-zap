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
import codezap.fixture.SourceCodeFixture;
import codezap.member.domain.Member;

class ThumbnailTest {

    @Nested
    @DisplayName("Thumbnail 이 Template 을 가지고 있는지 조회")
    class HasTemplate {

        @Test
        @DisplayName("Thumbnail 이 Template 을 가지고 있는지 조회: 참")
        void hasTemplate() {
            Template template = createTemplateById(1L);
            SourceCode sourceCode = SourceCodeFixture.get(template, 1);

            Thumbnail thumbnail = new Thumbnail(template, sourceCode);
            assertTrue(thumbnail.hasTemplate(template));
        }

        @Test
        @DisplayName("Thumbnail 이 Template 을 가지고 있는지 조회: 거짓")
        void hasNotTemplate() {
            Template template1 = createTemplateById(1L);
            SourceCode sourceCode1 = SourceCodeFixture.get(template1, 1);
            Thumbnail thumbnail1 = new Thumbnail(template1, sourceCode1);

            Template template2 = createTemplateById(2L);
            SourceCode sourceCode2 = SourceCodeFixture.get(template2, 1);
            Thumbnail thumbnail2 = new Thumbnail(template2, sourceCode2);

            assertFalse(thumbnail1.hasTemplate(template2));
        }

        private Template createTemplateById(Long id) {
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getFirstCategory();
            List<SourceCode> sourceCodes = List.of();
            long likesCount = 1L;
            return new Template(id, member, "Template 1", "Description 1", category, sourceCodes, likesCount);
        }
    }
}
