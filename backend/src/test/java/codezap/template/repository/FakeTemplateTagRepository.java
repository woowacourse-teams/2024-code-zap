package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

public class FakeTemplateTagRepository implements TemplateTagRepository {

    private final List<TemplateTag> templateTags;

    public FakeTemplateTagRepository() {
        this.templateTags = new ArrayList<>();
    }

    @Override
    public List<TemplateTag> findAllByTemplate(Template template) {
        return templateTags.stream()
                .filter(templateTag -> Objects.equals(templateTag.getTemplate(), template))
                .toList();
    }

    @Override
    public List<TemplateTag> findByTagIn(List<Tag> tags) {
        return List.of();
    }

    @Override
    public TemplateTag save(TemplateTag entity) {
        var saved = new TemplateTag(
                entity.getTemplate(),
                entity.getTag()
        );
        templateTags.removeIf(templateTag -> Objects.equals(templateTag.getId(), entity.getId()));
        templateTags.add(saved);
        return saved;
    }

    @Override
    public List<TemplateTag> saveAll(List<TemplateTag> templateTags) {
        return templateTags.stream().map(this::save).toList();
    }

    @Override
    public void deleteAllByTemplateId(Long id) {
        templateTags.removeIf(templateTag -> Objects.equals(templateTag.getTemplate().getId(), id));
    }
}
