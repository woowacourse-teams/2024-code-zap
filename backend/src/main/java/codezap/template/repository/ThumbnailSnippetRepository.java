package codezap.template.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.template.domain.Template;
import codezap.template.domain.ThumbnailSnippet;

public interface ThumbnailSnippetRepository extends JpaRepository<ThumbnailSnippet, Long> {

    ThumbnailSnippet findByTemplate(Template template);
    Optional<ThumbnailSnippet> findByTemplate(Template template);

    void deleteByTemplateId(Long id);

    @Query("""
            SELECT t
            FROM ThumbnailSnippet t JOIN Snippet s ON t.template.id = s.template.id
            WHERE t.template.title LIKE %:topic%
            OR s.filename LIKE %:topic%
            OR s.content LIKE %:topic%
            OR t.template.description LIKE %:topic%
            """)
    List<ThumbnailSnippet> searchByTopic(@Param("topic") String topic);
}
