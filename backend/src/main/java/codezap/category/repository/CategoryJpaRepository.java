package codezap.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;

@SuppressWarnings("unused")
public interface CategoryJpaRepository extends CategoryRepository, JpaRepository<Category, Long> {

    default Category fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 카테고리가 존재하지 않습니다."));
    }

    List<Category> findAllByMemberOrderById(Member member);

    boolean existsByNameAndMember(String categoryName, Member member);
}
