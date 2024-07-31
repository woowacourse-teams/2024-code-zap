package codezap.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    default Category fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 카테고리가 존재하지 않습니다."));
    }

    boolean existsByName(String name);
}
