package codezap.thumbnail_snippet.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import codezap.global.domain.BaseTimeEntity;
import codezap.snippet.domain.Snippet;
import codezap.template.domain.Template;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "representative_snippet")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailSnippet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @OneToOne
    @JoinColumn(name = "snippet_id", nullable = false)
    private Snippet snippet;
}
