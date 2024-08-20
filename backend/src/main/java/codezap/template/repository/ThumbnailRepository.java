package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

public interface ThumbnailRepository {

    Thumbnail fetchById(Long id);

    Thumbnail fetchByTemplate(Template template);

    Optional<Thumbnail> findByTemplate(Template template);

    List<Thumbnail> findAll();

    Thumbnail save(Thumbnail thumbnail);

    void deleteByTemplateId(Long id);
}
