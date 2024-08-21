package codezap.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;

@SuppressWarnings("unused")
public interface CategoryJpaRepository extends CategoryRepository, JpaRepository<Category, Long> {

    default Category fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "식별자 " + id + "에 해당하는 카테고리가 존재하지 않습니다."));
    }

    List<Category> findAllByMemberOrderById(Member member);

    boolean existsByNameAndMember(String categoryName, Member member);
}
