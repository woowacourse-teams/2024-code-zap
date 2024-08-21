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

import codezap.category.domain.Category;
import codezap.global.auditing.BaseTimeEntity;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
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

    public void validateAuthorization(Member member) {
        if (!member.equals(this.member)) {
            throw new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "해당 템플릿에 대한 권한이 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Template template = (Template) o;
        return Objects.equals(getId(), template.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
