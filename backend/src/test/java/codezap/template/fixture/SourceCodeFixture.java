package codezap.template.fixture;

import codezap.template.domain.SourceCode;

public class SourceCodeFixture {
    public static SourceCode getFirst() {
        return new SourceCode(
                1L,
                TemplateFixture.getFirst(),
                "sourceCode1",
                """
                        public class SourceCodeFixture {\n
                           public static SourceCode getFirst() {\n ...
                        """,
                1
        );
    }

    public static SourceCode getSecond() {
        return new SourceCode(
                2L,
                TemplateFixture.getFirst(),
                "sourceCode2",
                """
                        public class SourceCodeFixture {\n
                           public static SourceCode getFirst() {\n ...
                        """,
                2
        );
    }

    public static SourceCode getThird() {
        return new SourceCode(
                3L,
                TemplateFixture.getSecond(),
                "sourceCode2",
                """
                        public class SourceCodeFixture {\n
                           public static SourceCode getFirst() {\n ...
                        """,
                1
        );
    }
}
