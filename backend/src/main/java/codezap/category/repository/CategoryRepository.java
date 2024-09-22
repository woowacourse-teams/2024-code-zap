package codezap.category.repository;

import java.util.List;

import codezap.category.domain.Category;
import codezap.member.domain.Member;

public interface CategoryRepository {

    Category fetchById(Long id);

    List<Category> findAllByMemberIdOrderById(Member member);

    List<Category> findAll();

    boolean existsById(Long categoryId);

    boolean existsByNameAndMember(String categoryName, Member member);

    Category save(Category category);

    void deleteById(Long id);
}
