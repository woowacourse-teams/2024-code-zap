package codezap.template.repository;

import java.util.List;

import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

public interface TemplateTagRepository {

    List<TemplateTag> findAllByTemplate(Template template);

    TemplateTag save(TemplateTag templateTag);

    <S extends TemplateTag> List<S> saveAll(Iterable<S> entities);


    void deleteAllByTemplateId(Long id);

    List<TemplateTag> findAll();

    List<TemplateTag> findByTemplateIn(List<Template> templates);

    List<Long> findAllTemplateIdInTagIds(List<Long> tagIds, long tagSize);
}
