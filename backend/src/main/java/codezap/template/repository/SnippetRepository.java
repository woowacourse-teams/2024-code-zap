package codezap.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.Snippet;
import codezap.template.domain.Template;

public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    List<Snippet> findAllByTemplate(Template template);
}
