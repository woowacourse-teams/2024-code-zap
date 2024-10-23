package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

@SuppressWarnings("unused")
public interface ThumbnailJpaRepository extends
        ThumbnailRepository, JpaRepository<Thumbnail, Long> {

    default Thumbnail fetchByTemplate(Template template) {
        return findByTemplate(template).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND,
                        "식별자가 " + template.getId() + "인 템플릿에 해당하는 썸네일이 없습니다."));
    }

    @Query("""
            SELECT t, sc
            FROM Thumbnail t
            join fetch t.sourceCode sc
            WHERE t.template = :template
            """)
    Optional<Thumbnail> findByTemplate(@Param("template") Template template);


    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Thumbnail t WHERE t.template.id in :templateIds")
    void deleteAllByTemplateIds(@Param("templateIds") List<Long> templateIds);

    @Query("""
            SELECT t, sc
            FROM Thumbnail t
            join fetch t.sourceCode sc
            WHERE t.template.id IN :templateIds
            """)
    List<Thumbnail> findAllByTemplateIn(@Param("templateIds") List<Long> templateIds);

    void deleteByTemplateId(Long id);

}
