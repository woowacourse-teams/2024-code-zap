package codezap.template.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import codezap.template.domain.Template;

public interface TemplateRepository {

    Template fetchById(Long id);

    boolean existsByCategoryId(Long categoryId);

    @Query("""
            SELECT DISTINCT t
            FROM Template t JOIN Snippet s ON t.id = s.template.id
            WHERE t.member.id = :memberId AND
            (
                t.title LIKE :keyword
                OR s.filename LIKE :keyword
                OR s.content LIKE :keyword
                OR t.description LIKE :keyword
            )
            """)
    Page<Template> searchBy(
            @Param("memberId") Long memberId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT t
            FROM Template t JOIN Snippet s ON t.id = s.template.id
            WHERE t.member.id = :memberId AND
            t.id in :templateIds AND
            (
                t.title LIKE :keyword
                OR s.filename LIKE :keyword
                OR s.content LIKE :keyword
                OR t.description LIKE :keyword
            )
            """)
    Page<Template> searchBy(
            @Param("memberId") Long memberId,
            @Param("keyword") String keyword,
            @Param("templateIds") List<Long> templateIds,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT t
            FROM Template t JOIN Snippet s ON t.id = s.template.id
            WHERE t.member.id = :memberId AND
            t.category.id = :categoryId AND
            (
                t.title LIKE :keyword
                OR s.filename LIKE :keyword
                OR s.content LIKE :keyword
                OR t.description LIKE :keyword
            )
            """)
    Page<Template> searchBy(
            @Param("memberId") Long memberId,
            @Param("keyword") String keyword,
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
                t.title LIKE :keyword
                OR s.filename LIKE :keyword
                OR s.content LIKE :keyword
                OR t.description LIKE :keyword
            )
            """)
    Page<Template> searchBy(
            @Param("memberId") Long memberId,
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("templateIds") List<Long> templateIds,
            Pageable pageable
    );

    long count();

    Template save(Template template);

    List<Template> saveAll(List<Template> templates);

    void deleteById(Long id);
}
