package codezap.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Template;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    default Template fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 템플릿이 존재하지 않습니다."));
    }

    boolean existsByCategoryId(Long categoryId);

    @Query("""
            SELECT t
            FROM Template t JOIN Snippet s ON t.id = s.template.id
            WHERE
            (
                t.title LIKE %:topic%
                OR s.filename LIKE %:topic%
                OR s.content LIKE %:topic%
                OR t.description LIKE %:topic%
            )
            """)
    List<Template> searchByTopic(@Param("topic") String topic);

    Page<Template> findBy(Pageable pageable);

    Page<Template> findByCategoryId(Pageable pageable, Long categoryId);

    Page<Template> findByIdIn(PageRequest pageRequest, List<Long> templateIds);

    Page<Template> findByIdInAndCategoryId(PageRequest pageRequest, List<Long> templateIds, Long categoryId);

    long countByCategoryId(Long categoryId);

    long countByIdInAndCategoryId(List<Long> templateIds, Long categoryId);

    long countByIdIn(List<Long> templateIds);

}
