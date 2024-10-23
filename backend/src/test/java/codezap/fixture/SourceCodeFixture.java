package codezap.fixture;

import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;

public class SourceCodeFixture {

    public static SourceCode get(Template template, int ordinal) {
        return new SourceCode(
                template,
                "file1.java",
                "content1",
                ordinal
        );
    }
}
