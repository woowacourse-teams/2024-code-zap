package codezap.template.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.persistence.Embeddable;

@Embeddable
public record SourceCode(
        String filename,
        String content,
        int ordinal
) {
    private static final String LINE_BREAK = "\n";
    private static final int THUMBNAIL_LINE_HEIGHT = 5;

    public SourceCode(String filename, String content, Integer ordinal) {
        this(filename, content, ordinal.intValue());
    }

    public String getThumbnailContent() {
        return Arrays.stream(content.split(LINE_BREAK))
                .limit(THUMBNAIL_LINE_HEIGHT)
                .collect(Collectors.joining(LINE_BREAK));
    }

    public SourceCode update(String filename, String content, Integer ordinal) {
        return new SourceCode(filename, content, ordinal.intValue());
    }
}
