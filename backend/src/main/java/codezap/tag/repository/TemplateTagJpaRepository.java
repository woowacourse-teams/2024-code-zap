package codezap.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import codezap.tag.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

@SuppressWarnings("unused")
public interface TemplateTagJpaRepository extends TemplateTagRepository, JpaRepository<TemplateTag, Long> {

    @Query("""
            SELECT t
            FROM Tag t
            JOIN TemplateTag tt ON t.id = tt.id.tagId
            WHERE tt.template = :template
            """)
    List<Tag> findAllTagsByTemplate(Template template);

    @Query("""
            SELECT DISTINCT tt.id.tagId
            FROM TemplateTag tt
            WHERE tt.id.templateId IN (
                SELECT t.id FROM Template t WHERE t.member.id = :memberId
            )
            """)
    List<Long> findAllTagIdDistinctByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TemplateTag t WHERE t.template.id in :templateIds")
    void deleteByTemplateIds(List<Long> templateIds);
}
