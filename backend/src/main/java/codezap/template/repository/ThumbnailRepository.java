package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

public interface ThumbnailRepository {

    Thumbnail fetchByTemplate(Template template);

    Optional<Thumbnail> findByTemplate(Template template);

    List<Thumbnail> findAll();

    List<Thumbnail> findAllByTemplateIn(List<Long> templateIds);

    Thumbnail save(Thumbnail thumbnail);

    void deleteAllByTemplateIds(List<Long> ids);
}
