package codezap.category.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

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
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean isDefault;

    public Category(String name, Member member) {
        this.name = name;
        this.member = member;
    }

    public static Category createDefaultCategory(Member member) {
        return new Category(null, member, "카테고리 없음", true);
    }

    public void updateName(String name) {
        this.name = name;
    }
}
