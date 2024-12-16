package codezap.template.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import codezap.category.domain.Category;
import codezap.global.auditing.SkipModifiedAtBaseTimeEntity;
import codezap.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Template extends SkipModifiedAtBaseTimeEntity {

    private static final Long LIKES_COUNT_DEFAULT = 0L;

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

    @Column
    @ColumnDefault("0")
    private Long likesCount;

    @Column(nullable = false)
    @ColumnDefault("'PUBLIC'")
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    public Template(Member member, String title, String description, Category category) {
        this(member, title, description, category, Visibility.PUBLIC);
    }

    public Template(Member member, String title, String description, Category category, Visibility visibility) {
        this(null, member, title, description, category, null, 0L, visibility);
    }

    public void updateTemplate(String title, String description, Category category, Visibility visibility) {
        this.modifiedAt = LocalDateTime.now();
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
    }

    public boolean matchMember(Member member) {
        return this.member.equals(member);
    }

    public boolean isPrivate() {
        return visibility == Visibility.PRIVATE;
    }

    public void increaseLike() {
        skipModifiedAtUpdate();
        this.likesCount++;
    }

    public void cancelLike() {
        skipModifiedAtUpdate();
        if (this.likesCount <= LIKES_COUNT_DEFAULT) {
            return;
        }
        this.likesCount--;
    }
}
