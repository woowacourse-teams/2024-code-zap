package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Snippet;
import codezap.template.domain.Template;

public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    default Snippet fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 스니펫이 존재하지 않습니다."));
    }

    List<Snippet> findAllByTemplate(Template template);

    Optional<Snippet> findByTemplateAndOrdinal(Template template, int ordinal);

    List<Snippet> findAllByTemplateAndOrdinal(Template template, int ordinal);

    void deleteByTemplateId(Long id);
}
