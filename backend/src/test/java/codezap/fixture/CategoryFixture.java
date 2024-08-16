package codezap.fixture;

import codezap.category.domain.Category;

public class CategoryFixture {
    public static Category getFirstCategory() {
        return new Category(1L, MemberFixture.getFirstMember(), "카테고리 없음", true);
    }

    public static Category getSecondCategory() {
        return new Category(2L, MemberFixture.getSecondMember(), "자바", true);
    }
}
