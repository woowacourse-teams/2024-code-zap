package codezap.representative_snippet.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import codezap.global.domain.BaseTimeEntity;
import codezap.snippet.domain.Snippet;
import codezap.template.domain.Template;
import lombok.Getter;

@Entity
@Table(name = "representative_snippet")
@Getter
public class RepresentativeSnippet extends BaseTimeEntity {

    @Id
    private Long templateId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @OneToOne
    @JoinColumn(name = "snippet_id", nullable = false)
    private Snippet snippet;
}
