package codezap.template.fixture;

import codezap.tag.domain.Tag;

public class TagFixture {
    public static Tag getFirst() {
        return new Tag(
                1L,
                "태그1"
        );
    }

    public static Tag getSecond() {
        return new Tag(
                2L,
                "태그2"
        );
    }
}
