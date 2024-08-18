package codezap.template.fixture;

import java.util.List;

import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.template.domain.Template;

public class TemplateFixture {
    public static Template getFirst() {
        return new Template(
                1L,
                MemberFixture.getFirstMember(),
                "title",
                "description",
                CategoryFixture.getFirstCategory(),
                List.of()
        );
    }

    public static Template getSecond() {
        return new Template(
                2L,
                MemberFixture.getFirstMember(),
                "title2",
                "description",
                CategoryFixture.getFirstCategory(),
                List.of()
        );
    }

    public static Template getThird() {
        return new Template(
                3L,
                MemberFixture.getSecondMember(),
                "title3",
                "description",
                CategoryFixture.getSecondCategory(),
                List.of()
        );
    }
}
