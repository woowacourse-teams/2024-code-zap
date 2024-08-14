package codezap.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Template;
import codezap.template.domain.ThumbnailSnippet;

@SuppressWarnings("unused")
public interface ThumbnailSnippetJpaRepository extends
        ThumbnailSnippetRepository, JpaRepository<ThumbnailSnippet, Long> {

    default ThumbnailSnippet fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 썸네일 스니펫이 존재하지 않습니다."));
    }

    Optional<ThumbnailSnippet> findByTemplate(Template template);

    void deleteByTemplateId(Long id);
}
