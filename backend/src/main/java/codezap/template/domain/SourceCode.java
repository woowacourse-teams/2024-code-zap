package codezap.template.domain;

import java.util.Arrays;
import java.util.Objects;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class SourceCode extends BaseTimeEntity {

    private static final String LINE_BREAK = "\n";
    private static final int THUMBNAIL_LINE_HEIGHT = 5;

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

    public SourceCode(Template template, String filename, String content, Integer ordinal) {
        this.template = template;
        this.filename = filename;
        this.content = content;
        this.ordinal = ordinal;
    }

    public String getThumbnailContent() {
        return Arrays.stream(content.split(LINE_BREAK))
                .limit(THUMBNAIL_LINE_HEIGHT)
                .collect(Collectors.joining(LINE_BREAK));
    }

    public void updateSourceCode(String filename, String content, Integer ordinal) {
        this.filename = filename;
        this.content = content;
        this.ordinal = ordinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SourceCode that = (SourceCode) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
