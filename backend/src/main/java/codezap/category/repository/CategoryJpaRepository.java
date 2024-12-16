package codezap.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.category.domain.Category;
import codezap.member.domain.Member;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByMemberIdOrderById(Long memberId);

    boolean existsByNameAndMember(String categoryName, Member member);

    long countByMember(Member member);
}
