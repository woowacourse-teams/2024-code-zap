package codezap.tag.repository;

import java.util.List;

import codezap.tag.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

public interface TemplateTagRepository {

    List<TemplateTag> findAllByTemplate(Template template);

    List<Tag> findAllTagsByTemplate(Template template);

    List<Long> findDistinctByTemplateIn(List<Long> templateIds);

    TemplateTag save(TemplateTag templateTag);

    <S extends TemplateTag> List<S> saveAll(Iterable<S> entities);

    void deleteAllByTemplateId(Long id);
}
