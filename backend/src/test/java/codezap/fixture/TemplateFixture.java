package codezap.fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import codezap.category.domain.Category;
import codezap.member.domain.Member;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;

public class TemplateFixture {
    public static Template get(Member member, Category category) {
        return new Template(member, "안녕", "Description 1", category, Visibility.PUBLIC, getList(1));
    }

    public static Template getPrivate(Member member, Category category) {
        return new Template(member, "안녕하세요", "Description 1", category, Visibility.PRIVATE, getList(1));
    }

    public static List<Template> getList(int size, Member member, Category category) {
        List<Template> templates = new ArrayList<>();
        IntStream.range(0, size).forEach(i -> templates.add(new Template(
                (long) i + 1,
                member,
                "title" + i,
                "description" + i,
                category,
                0L,
                Visibility.PUBLIC,
                0L,
                getList(1)
        )));
        return templates;
    }

    private static List<SourceCode> getList(int size) {
        List<SourceCode> sourceCodes = new ArrayList<>();
        IntStream.range(0, size).forEach(i -> sourceCodes.add(new SourceCode(
                "file" + i + ".java",
                "content" + i
        )));
        return sourceCodes;
    }
}
