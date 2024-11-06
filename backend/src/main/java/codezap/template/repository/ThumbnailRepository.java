package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ThumbnailRepository {

    private final ThumbnailJpaRepository thumbnailJpaRepository;
    private final ThumbnailQueryDSLRepository thumbnailQueryDSLRepository;

    public Thumbnail fetchByTemplate(Template template) {
        return thumbnailQueryDSLRepository.findByTemplate(template).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자가 " + template.getId() + "인 템플릿에 해당하는 썸네일이 없습니다."));
    }

    public Optional<Thumbnail> findByTemplate(Template template) {
        return thumbnailQueryDSLRepository.findByTemplate(template);
    }

    public List<Thumbnail> findAll() {
        return thumbnailJpaRepository.findAll();
    }

    public List<Thumbnail> findAllByTemplateIn(List<Long> templateIds) {
        return thumbnailQueryDSLRepository.findAllByTemplateIn(templateIds);
    }

    public Thumbnail save(Thumbnail thumbnail) {
        return thumbnailJpaRepository.save(thumbnail);
    }

    public void deleteAllByTemplateIds(List<Long> ids) {
        thumbnailQueryDSLRepository.deleteAllByTemplateIds(ids);
    }
}
