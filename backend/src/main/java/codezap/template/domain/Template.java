package codezap.template.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.Hibernate;

import codezap.category.domain.Category;
import codezap.global.auditing.BaseTimeEntity;
import codezap.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Template extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = false)
    private Category category;

    @OneToMany(mappedBy = "template")
    private List<SourceCode> sourceCodes = new ArrayList<>();

    public Template(Member member, String title, String description, Category category) {
        this.member = member;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public void updateTemplate(String title, String description, Category category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public void updateSourceCodes(List<SourceCode> sourceCode) {
        sourceCodes.addAll(sourceCode);
    }

    public void deleteSourceCode(Long deletedId) {
        sourceCodes.removeIf(sourceCode -> Objects.equals(sourceCode.getId(), deletedId));
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
        Template template = (Template) o;
        return getId() != null && Objects.equals(getId(), template.getId());
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
