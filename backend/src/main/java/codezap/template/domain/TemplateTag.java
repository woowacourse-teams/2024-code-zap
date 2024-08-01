package codezap.template.domain;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import codezap.global.auditing.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class TemplateTag extends BaseTimeEntity {

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    private static class TemplateTagId implements Serializable {
        private Long templateId;
        private Long tagId;
    }

    @EmbeddedId
    private TemplateTagId id;

    @ManyToOne
    @MapsId("templateId")
    @JoinColumn(name = "template_id")
    private Template template;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public TemplateTag(Template template, Tag tag) {
        this.id = new TemplateTagId(template.getId(), tag.getId());
        this.template = template;
        this.tag = tag;
    }
}
