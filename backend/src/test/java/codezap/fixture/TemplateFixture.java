package codezap.fixture;

import codezap.category.domain.Category;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;

public class TemplateFixture {
    public static Template get(Member member, Category category) {
        return new Template(member, "안녕", "Description 1", category);
    }

    public static Template getPrivate(Member member, Category category) {
        return new Template(member, "안녕하세요", "Description 1", category, Visibility.PRIVATE);
    }
}
