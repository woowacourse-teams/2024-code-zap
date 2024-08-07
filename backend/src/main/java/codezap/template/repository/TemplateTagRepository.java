package codezap.template.repository;

import java.util.List;

import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

public interface TemplateTagRepository {

    List<TemplateTag> findAllByTemplate(Template template);

    List<TemplateTag> findByTagIn(List<Tag> tags);

    TemplateTag save(TemplateTag templateTag);

    <S extends TemplateTag> List<S> saveAll(Iterable<S> entities);


    void deleteAllByTemplateId(Long id);

    List<TemplateTag> findAll();
}
