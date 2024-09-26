package codezap.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

@SuppressWarnings("unused")
public interface ThumbnailJpaRepository extends
        ThumbnailRepository, JpaRepository<Thumbnail, Long> {

    default Thumbnail fetchByTemplate(Template template) {
        return findByTemplate(template).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND,
                        "식별자가 " + template.getId() + "인 템플릿에 해당하는 썸네일이 없습니다."));
    }

    Optional<Thumbnail> findByTemplate(Template template);

    void deleteByTemplateId(Long id);
}
