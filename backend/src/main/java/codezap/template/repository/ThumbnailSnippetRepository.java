package codezap.template.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.Template;
import codezap.template.domain.ThumbnailSnippet;

public interface ThumbnailSnippetRepository extends JpaRepository<ThumbnailSnippet, Long> {

    ThumbnailSnippet findByTemplate(Template template);
    Optional<ThumbnailSnippet> findByTemplate(Template template);

    void deleteByTemplateId(Long id);
}
