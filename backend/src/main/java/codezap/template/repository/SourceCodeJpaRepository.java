package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;

@SuppressWarnings("unused")
public interface SourceCodeJpaRepository extends JpaRepository<SourceCode, Long> {

    List<SourceCode> findAllByTemplate(Template template);

    Optional<SourceCode> findByTemplateAndOrdinal(Template template, int ordinal);

    List<SourceCode> findAllByTemplateAndOrdinal(Template template, int ordinal);

    int countByTemplate(Template template);
}
