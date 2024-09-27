package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;

public interface SourceCodeRepository {
    SourceCode fetchById(Long id);

    List<SourceCode> findAllByTemplate(Template template);

    SourceCode fetchByTemplateAndOrdinal(Template template, int ordinal);

    Optional<SourceCode> findByTemplateAndOrdinal(Template template, int ordinal);

    List<SourceCode> findAllByTemplateAndOrdinal(Template template, int ordinal);

    int countByTemplate(Template template);

    SourceCode save(SourceCode sourceCode);

    <S extends SourceCode> List<S> saveAll(Iterable<S> entities);

    void deleteById(Long id);

    void deleteByTemplateIds(List<Long> templateIds);
}
