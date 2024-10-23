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
@EqualsAndHashCode(of = "id", callSuper = false)
public class Thumbnail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Template template;

    @OneToOne(optional = false)
    private SourceCode sourceCode;

    public Thumbnail(Template template, SourceCode sourceCode) {
        this.template = template;
        this.sourceCode = sourceCode;
    }

    public void updateThumbnail(SourceCode sourceCode) {
        this.sourceCode = sourceCode;
    }

    public boolean hasTemplate(Template template) {
        return this.template.equals(template);
    }
}
