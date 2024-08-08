package codezap.template.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

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
public class ThumbnailSnippet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Template template;

    @OneToOne(optional = false)
    private Snippet snippet;

    public ThumbnailSnippet(Template template, Snippet snippet) {
        this.template = template;
        this.snippet = snippet;
    }

    public void updateThumbnailSnippet(Snippet snippet) {
        this.snippet = snippet;
    }
}
