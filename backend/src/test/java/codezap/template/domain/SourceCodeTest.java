package codezap.template.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.member.domain.Member;

class SourceCodeTest {

    @Test
    @DisplayName("성공: 썸네일(5줄) 추출 잘 되는지 확인")
    void getThumbnailContent() {
        // given
        Member member = new Member(1L, "codezap@code.zap", "1234", "zappy");
        Category category = new Category(1L, member, "category", false);
        Template template = new Template(member, "title", "description", category);
        SourceCode sourceCode = new SourceCode(
                1L,
                template,
                "file",
                "1\n2\n3\n4\n5\n6\n7\n8\n9\n",
                1
        );

        // when
        String thumbnail = sourceCode.getThumbnailContent();

        // then
        assertThat(thumbnail).isEqualTo("1\n2\n3\n4\n5");
    }
}
