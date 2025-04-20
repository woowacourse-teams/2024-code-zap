package codezap.template.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import codezap.category.domain.Category;
import codezap.global.auditing.SkipModifiedAtBaseTimeEntity;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Template extends SkipModifiedAtBaseTimeEntity {

    private static final Long LIKES_COUNT_DEFAULT = 0L;
    private static final int MINIMUM_SOURCE_CODE_COUNT = 1;

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

    @Column
    @ColumnDefault("0")
    private Long likesCount;

    @Column(nullable = false)
    @ColumnDefault("'PUBLIC'")
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long thumbnailOrdinal;

    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn(name = "ordinal")
    private final List<SourceCode> sourceCodes = new ArrayList<>();

    public Template(Long id, Member member, String title, String description, Category category, Long likesCount, Visibility visibility, Long thumbnailOrdinal) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.description = description;
        this.category = category;
        this.likesCount = likesCount;
        this.visibility = visibility;
        this.thumbnailOrdinal = thumbnailOrdinal;
        validateSourceCodeCount(sourceCodes);
    }

    public Template(Member member, String title, String description, Category category, Visibility visibility) {
        this(null, member, title, description, category, 0L, visibility, 0L);
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

    public SourceCode getThumbnailSourceCode() {
        return sourceCodes.get(thumbnailOrdinal.intValue());
    }

    private void validateSourceCodeCount(List<SourceCode> sourceCodes) {
        if (sourceCodes.size() < MINIMUM_SOURCE_CODE_COUNT) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "소스 코드는 최소 1개 입력해야 합니다.");
        }
    }

    public void updateThumbnailCode(Long thumbnailOrdinal) {
        this.thumbnailOrdinal = thumbnailOrdinal;
    }
}
