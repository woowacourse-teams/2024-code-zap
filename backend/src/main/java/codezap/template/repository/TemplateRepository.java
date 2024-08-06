package codezap.template.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Template;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    default Template fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 템플릿이 존재하지 않습니다."));
    }

    boolean existsByCategoryId(Long categoryId);

    Page<Template> findBy(Pageable pageable);

    Page<Template> findByCategoryId(Pageable pageable, Long categoryId);

    Page<Template> findByIdIn(PageRequest pageRequest, List<Long> templateIds);

    Page<Template> findByIdInAndCategoryId(PageRequest pageRequest, List<Long> templateIds, Long categoryId);

    long countByCategoryId(Long categoryId);

    long countByIdInAndCategoryId(List<Long> templateIds, Long categoryId);

    long countByIdIn(List<Long> templateIds);

}
