package codezap.category.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import codezap.global.auditing.BaseTimeEntity;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "name_with_member",
                columnNames = {"member_id", "name"}
        ),
        indexes = @Index(name = "idx_member_id", columnList = "member_id")
)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Category extends BaseTimeEntity {

    private static final String DEFAULT_CATEGORY_NAME = "카테고리 없음";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isDefault;

    @Column(nullable = false)
    private long ordinal;

    public Category(String name, Member member, long ordinal) {
        this.name = name;
        this.member = member;
        this.isDefault = false;
        this.ordinal = ordinal;
    }

    public static Category createDefaultCategory(Member member) {
        return new Category(null, member, DEFAULT_CATEGORY_NAME, true, 1);
    }

    public void update(String name, long ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public void validateAuthorization(Member member) {
        if (!getMember().equals(member)) {
            throw new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }
    }

    public boolean isDefault() {
        return isDefault;
    }
}
