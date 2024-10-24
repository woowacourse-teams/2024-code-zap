package codezap.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;

@SuppressWarnings("unused")
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByMemberIdOrderById(Long memberId);

    boolean existsByNameAndMember(String categoryName, Member member);
}
