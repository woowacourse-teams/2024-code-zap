package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import codezap.template.domain.Snippet;
import codezap.template.domain.Template;

public interface SnippetRepository {
    Snippet fetchById(Long id);

    List<Snippet> findAll();

    List<Snippet> findAllByTemplate(Template template);

    Optional<Snippet> findByTemplateAndOrdinal(Template template, int ordinal);

    List<Snippet> findAllByTemplateAndOrdinal(Template template, int ordinal);

    Snippet save(Snippet snippet);

    List<Snippet> saveAll(List<Snippet> snippets);

    void deleteById(Long id);

    void deleteByTemplateId(Long id);
}
