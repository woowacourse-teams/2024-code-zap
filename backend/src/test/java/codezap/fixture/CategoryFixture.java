package codezap.fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import codezap.category.domain.Category;
import codezap.member.domain.Member;

public class CategoryFixture {
    public static Category getDefaultCategory(Member member) {
        return new Category(1L, member, "카테고리 없음", true, 1);
    }

    public static Category getCategory(Member member) {
        return new Category(2L, member, "카테고리", false, 2);
    }

    public static List<Category> getList(Member member, int size) {
        List<Category> categories = new ArrayList<>();
        categories.add(getDefaultCategory(member));
        IntStream.range(1, size).forEach(i ->  categories.add(new Category("카테고리" + i, member, i + 1)));
        return categories;
    }
}
