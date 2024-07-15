package codezap.snippet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import codezap.extension.domain.Extension;
import codezap.global.domain.BaseTimeEntity;
import codezap.template.domain.Template;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "snippet")
@Getter
@Setter
public class Snippet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extension_id", nullable = false)
    private Extension extension;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer ordinal;
}
