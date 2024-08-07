package codezap.template.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            SELECT DISTINCT t
            FROM Template t JOIN Snippet s ON t.id = s.template.id
            WHERE t.member.id = :memberId AND
            (
                t.title LIKE :topic
                OR s.filename LIKE :topic
                OR s.content LIKE :topic
                OR t.description LIKE :topic
            )
            """)
    Page<Template> searchBy(
            @Param("memberId") Long memberId,
            @Param("topic") String topic,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT t
            FROM Template t JOIN Snippet s ON t.id = s.template.id
            WHERE t.member.id = :memberId AND
            t.id in :templateIds AND
            (
                t.title LIKE :topic
                OR s.filename LIKE :topic
                OR s.content LIKE :topic
                OR t.description LIKE :topic
            )
            """)
    Page<Template> searchBy(
            @Param("memberId") Long memberId,
            @Param("topic") String topic,
            @Param("templateIds") List<Long> templateIds,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT t
            FROM Template t JOIN Snippet s ON t.id = s.template.id
            WHERE t.member.id = :memberId AND
            t.category.id = :categoryId AND
            (
                t.title LIKE :topic
                OR s.filename LIKE :topic
                OR s.content LIKE :topic
                OR t.description LIKE :topic
            )
            """)
    Page<Template> searchBy(
            @Param("memberId") Long memberId,
            @Param("topic") String topic,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT t
            FROM Template t JOIN Snippet s ON t.id = s.template.id
            WHERE t.member.id = :memberId AND
            t.category.id = :categoryId AND
            t.id in :templateIds AND
            (
                t.title LIKE :topic
                OR s.filename LIKE :topic
                OR s.content LIKE :topic
                OR t.description LIKE :topic
            )
            """)
    Page<Template> searchBy(
            @Param("memberId") Long memberId,
            @Param("topic") String topic,
            @Param("categoryId") Long categoryId,
            @Param("templateIds") List<Long> templateIds,
            Pageable pageable
    );

    Page<Template> findBy(Pageable pageable);

    Page<Template> findByCategoryId(Pageable pageable, Long categoryId);

    Page<Template> findByIdIn(PageRequest pageRequest, List<Long> templateIds);

    Page<Template> findByIdInAndCategoryId(PageRequest pageRequest, List<Long> templateIds, Long categoryId);

    long countByCategoryId(Long categoryId);

    long countByIdInAndCategoryId(List<Long> templateIds, Long categoryId);

    long countByIdIn(List<Long> templateIds);

}
