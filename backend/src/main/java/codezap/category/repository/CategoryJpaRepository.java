package codezap.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.category.domain.Category;
import codezap.member.domain.Member;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByMemberIdOrderById(Long memberId);

    boolean existsByNameAndMember(String categoryName, Member member);

    long countByMember(Member member);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Category c WHERE c.id = :id")
    void deleteByIdWithFlush(@Param("id") Long id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Category c SET c.name = :name, c.ordinal = :ordinal WHERE c.id = :id")
    void updateCategoryWithFlush(@Param("id") Long id, @Param("name") String name, @Param("ordinal") int ordinal);

}
