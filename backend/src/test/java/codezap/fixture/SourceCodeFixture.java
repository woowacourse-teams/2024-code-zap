package codezap.fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

    public static List<SourceCode> getList(Template template, int size) {
        List<SourceCode> sourceCodes = new ArrayList<>();
        IntStream.range(0, size).forEach(i -> sourceCodes.add(get(template, i)));
        return sourceCodes;
    }
}
