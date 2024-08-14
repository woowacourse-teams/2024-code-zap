package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import codezap.template.domain.Snippet;
import codezap.template.domain.Template;

public interface SnippetRepository {
    Snippet fetchById(Long id);

    List<Snippet> findAllByTemplate(Template template);

    Snippet fetchByTemplateAndOrdinal(Template template, int ordinal);

    Optional<Snippet> findByTemplateAndOrdinal(Template template, int ordinal);

    List<Snippet> findAllByTemplateAndOrdinal(Template template, int ordinal);

    List<Snippet> findAll();

    Snippet save(Snippet snippet);

    <S extends Snippet> List<S> saveAll(Iterable<S> entities);

    void deleteById(Long id);

    void deleteByTemplateId(Long id);
}
