package codezap.template.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.Formula;
import org.springframework.http.HttpStatus;

import codezap.category.domain.Category;
import codezap.global.auditing.BaseTimeEntity;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Category category;

    @OneToMany(mappedBy = "template")
    private List<SourceCode> sourceCodes = new ArrayList<>();

    @Formula("(select count(*) from likes where likes.template_id = id)")
    private Long likesCount;

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

    public void validateAuthorization(Member member) {
        if (!getMember().equals(member)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "해당 템플릿에 대한 권한이 없습니다.");
        }
    }
}
