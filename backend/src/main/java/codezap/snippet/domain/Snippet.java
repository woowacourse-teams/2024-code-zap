package codezap.snippet.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import codezap.global.auditing.BaseTimeEntity;
import codezap.template.domain.Template;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "snippet")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Snippet extends BaseTimeEntity {

    private static final String CODE_LINE_BREAK = "\n";
    private static final int THUMBNAIL_SNIPPET_SIZE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer ordinal;

    public String getSummaryContent() {
        return Arrays.stream(content.split(CODE_LINE_BREAK))
                .limit(THUMBNAIL_SNIPPET_SIZE)
                .collect(Collectors.joining(CODE_LINE_BREAK));
    }
}
