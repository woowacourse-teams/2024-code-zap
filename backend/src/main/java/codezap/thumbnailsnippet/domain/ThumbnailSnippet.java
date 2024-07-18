package codezap.thumbnailsnippet.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import codezap.global.auditing.BaseTimeEntity;
import codezap.snippet.domain.Snippet;
import codezap.template.domain.Template;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailSnippet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @OneToOne
    @JoinColumn(name = "snippet_id", nullable = false)
    private Snippet snippet;
}
