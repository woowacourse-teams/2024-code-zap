package codezap.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

@SuppressWarnings("unused")
public interface TemplateTagJpaRepository extends TemplateTagRepository, JpaRepository<TemplateTag, Long> {

    List<TemplateTag> findAllByTemplate(Template template);

    List<TemplateTag> findDistinctByTemplateIn(List<Template> templates);

    @Query("""
            SELECT DISTINCT tt.id.templateId
            FROM TemplateTag tt
            WHERE tt.id.tagId IN :tagIds
            GROUP BY tt.id.templateId
            HAVING COUNT(DISTINCT tt.id.tagId) = :tagSize
            """)
    List<Long> findAllTemplateIdInTagIds(List<Long> tagIds, long tagSize);

    void deleteAllByTemplateId(Long id);
}
