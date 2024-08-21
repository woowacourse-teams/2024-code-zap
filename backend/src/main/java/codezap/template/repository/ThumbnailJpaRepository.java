package codezap.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

@SuppressWarnings("unused")
public interface ThumbnailJpaRepository extends
        ThumbnailRepository, JpaRepository<Thumbnail, Long> {

    default Thumbnail fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 썸네일이 존재하지 않습니다."));
    }

    default Thumbnail fetchByTemplate(Template template) {
        return findByTemplate(template).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND,
                        "식별자가 " + template.getId() + "인 템플릿에 해당하는 썸네일이 없습니다."));

    }

    Optional<Thumbnail> findByTemplate(Template template);

    void deleteByTemplateId(Long id);
}
