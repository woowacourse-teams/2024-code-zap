package codezap.template.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import codezap.global.auditing.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class Snippet extends BaseTimeEntity {

    private static final String LINE_BREAK = "\n";
    private static final int THUMBNAIL_SNIPPET_LINE_HEIGHT = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Template template;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer ordinal;

    public Snippet(Template template, String filename, String content, Integer ordinal) {
        this.template = template;
        this.filename = filename;
        this.content = content;
        this.ordinal = ordinal;
    }

    public String getThumbnailContent() {
        return Arrays.stream(content.split(LINE_BREAK))
                .limit(THUMBNAIL_SNIPPET_LINE_HEIGHT)
                .collect(Collectors.joining(LINE_BREAK));
    }

    public void updateSnippet(String filename, String content, Integer ordinal) {
        this.filename = filename;
        this.content = content;
        this.ordinal = ordinal;
    }
}
