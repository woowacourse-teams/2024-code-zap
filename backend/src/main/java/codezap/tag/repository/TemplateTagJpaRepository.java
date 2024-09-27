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
            SELECT DISTINCT t
            FROM Tag t
            WHERE t.id IN (
                SELECT DISTINCT tt.id.tagId
                FROM TemplateTag tt
                WHERE tt.id.templateId IN
                    (SELECT te.id FROM Template te WHERE te.member.id = :memberId)
            )
        SELECT tt, t
        FROM TemplateTag tt
        JOIN FETCH tt.tag t
        WHERE tt.id.templateId = :templateId
        """)
    List<TemplateTag> findAllByTemplateId(Long templateId);

    @Query("""
        SELECT tt, t
        FROM TemplateTag tt
        JOIN FETCH tt.tag t
        WHERE tt.id.templateId in :templateIds
        """)
    List<TemplateTag> findAllByTemplateIdsIn(List<Long> templateIds);

    @Query("""
            SELECT DISTINCT tt.id.tagId
            FROM TemplateTag tt
            WHERE tt.id.templateId IN :templateIds
            """)
    List<Tag> findAllTagDistinctByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TemplateTag t WHERE t.template.id in :templateIds")
    void deleteByTemplateIds(List<Long> templateIds);
}
