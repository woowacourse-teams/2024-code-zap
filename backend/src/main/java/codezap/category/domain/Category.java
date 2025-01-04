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
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "name_with_member",
                        columnNames = {"member_id", "name"}
                ),
                @UniqueConstraint(
                        name = "ordinal_with_member",
                        columnNames = {"member_id", "ordinal"}
                )
        },
        indexes = @Index(name = "idx_member_id", columnList = "member_id")
)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Category extends BaseTimeEntity {

    private static final String DEFAULT_CATEGORY_NAME = "카테고리 없음";
    private static final int DEFAULT_CATEGORY_ORDINAL = 0;

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
    private int ordinal;

    public Category(String name, Member member, int ordinal) {
        this(null, member, name, false, ordinal);
    }

    public static Category createDefaultCategory(Member member) {
        return new Category(null, member, DEFAULT_CATEGORY_NAME, true, DEFAULT_CATEGORY_ORDINAL);
    }

    public boolean hasAuthorization(Member member) {
        return this.member.equals(member);
    }

    public boolean isDefault() {
        return isDefault;
    }
}
