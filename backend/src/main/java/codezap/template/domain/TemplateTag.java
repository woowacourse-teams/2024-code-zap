package codezap.template.domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import org.hibernate.Hibernate;

import codezap.global.auditing.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TemplateTag extends BaseTimeEntity {

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Override
    @SuppressWarnings("all")
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass = Hibernate.getClass(o);
        Class<?> thisEffectiveClass = Hibernate.getClass(this);
        if (!thisEffectiveClass.equals(oEffectiveClass)) {
            return false;
        }
        TemplateTag that = (TemplateTag) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
