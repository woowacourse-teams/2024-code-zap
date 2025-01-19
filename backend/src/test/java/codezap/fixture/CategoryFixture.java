package codezap.fixture;

import codezap.category.domain.Category;
import codezap.member.domain.Member;

public class CategoryFixture {
    public static Category getFirstCategory() {
        return new Category(1L, MemberFixture.getFirstMember(), "카테고리 없음", true, 1);
    }

    public static Category getSecondCategory() {
        return new Category(2L, MemberFixture.getSecondMember(), "자바", true, 2);
    }

    public static Category get(Member member) {
        return new Category("카테고리", member, 1);
    }
}
