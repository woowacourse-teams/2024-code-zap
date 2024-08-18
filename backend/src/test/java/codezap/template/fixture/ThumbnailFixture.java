package codezap.template.fixture;

import codezap.template.domain.Thumbnail;

public class ThumbnailFixture {

    public static Thumbnail getSecond() {
        return new Thumbnail(
                2L,
                TemplateFixture.getSecond(),
                SourceCodeFixture.getThird()
        );
    }
}
