package codezap.category.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.springframework.http.HttpStatus;

import codezap.global.auditing.BaseTimeEntity;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "name_with_member",
                        columnNames = {"member_id", "name"}
                )
        }
)
public class Category extends BaseTimeEntity {

    private static final String DEFAULT_CATEGORY_NAME = "카테고리 없음";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isDefault;

    public Category(String name, Member member) {
        this.name = name;
        this.member = member;
        this.isDefault = false;
    }

    public static Category createDefaultCategory(Member member) {
        return new Category(null, member, DEFAULT_CATEGORY_NAME, true);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void validateAuthorization(Member member) {
        if (!getMember().equals(member)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "해당 카테고리에 대한 권한이 없습니다.");
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
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
