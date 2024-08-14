package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import codezap.template.domain.Template;
import codezap.template.domain.ThumbnailSnippet;

public interface ThumbnailSnippetRepository {

    ThumbnailSnippet fetchById(Long id);

    ThumbnailSnippet fetchByTemplate(Template template);

    Optional<ThumbnailSnippet> findByTemplate(Template template);

    List<ThumbnailSnippet> findAll();

    ThumbnailSnippet save(ThumbnailSnippet thumbnailSnippet);

    void deleteByTemplateId(Long id);
}
