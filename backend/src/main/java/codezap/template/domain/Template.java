package codezap.template.domain;

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
import codezap.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private List<Snippet> snippets;

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

    public void updateSnippets(List<Snippet> snippet) {
        snippets.addAll(snippet);
    }

    public void deleteSnippet(Long deletedId) {
        snippets.removeIf(snippet -> Objects.equals(snippet.getId(), deletedId));
    }
}
