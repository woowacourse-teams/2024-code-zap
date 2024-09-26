package codezap.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

@SuppressWarnings("unused")
public interface TemplateTagJpaRepository extends TemplateTagRepository, JpaRepository<TemplateTag, Long> {

    List<TemplateTag> findAllByTemplate(Template template);

    @Query("""
            SELECT DISTINCT tt.id.tagId
            FROM TemplateTag tt
            WHERE tt.id.templateId IN :templateIds
            """)
    List<Long> findDistinctByTemplateIn(List<Long> templateIds);

    void deleteAllByTemplateId(Long id);
}
