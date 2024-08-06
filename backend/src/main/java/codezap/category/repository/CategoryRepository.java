package codezap.category.repository;

import java.util.List;

import codezap.category.domain.Category;
import codezap.member.domain.Member;

public interface CategoryRepository {

    Category fetchById(Long id);

    List<Category> findAllByMember(Member member);

    List<Category> findAll();

    boolean existsByNameAndMember(String categoryName, Member member);

    Category save(Category category);

    void deleteById(Long id);
}
