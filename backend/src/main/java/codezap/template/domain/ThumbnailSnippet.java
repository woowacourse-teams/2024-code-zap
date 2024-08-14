package codezap.template.domain;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import org.hibernate.Hibernate;

import codezap.global.auditing.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
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
        ThumbnailSnippet that = (ThumbnailSnippet) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
