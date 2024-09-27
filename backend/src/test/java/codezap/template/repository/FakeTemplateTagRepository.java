package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import codezap.tag.domain.Tag;
import codezap.tag.repository.TemplateTagRepository;
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
    public List<Tag> findAllTagsByTemplate(Template template) {
        return templateTags.stream()
                .filter(templateTag -> Objects.equals(templateTag.getTemplate(), template))
                .map(TemplateTag::getTag)
                .toList();
    }

    public List<TemplateTag> findAllByTemplateId(Long templateId) {
        return templateTags.stream()
                .filter(templateTag -> Objects.equals(templateTag.getTemplate(), templateId))
                .toList();
    }

    @Override
    public List<Long> findDistinctByTemplateIn(List<Long> templateIds) {
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
    public <S extends TemplateTag> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
        return (List<S>) templateTags;
    }

    @Override
    public void deleteAllByTemplateId(Long templateId) {
        templateTags.removeIf(templateTag -> Objects.equals(templateTag.getId(), templateId));
    }

    @Override
    public void deleteByTemplateIds(List<Long> templateIds) {
        templateIds.forEach(id ->
                templateTags.removeIf(templateTag -> Objects.equals(templateTag.getId(), id)));
    }

    @Override
    public List<Tag> findAllTagDistinctByMemberId(Long memberId) {
        List<Long> templateIds = templateTags.stream()
                .filter(templateTag -> Objects.equals(templateTag.getTemplate().getMember().getId(), memberId))
                .map(templateTag -> templateTag.getTag().getId())
                .toList();

        return templateTags.stream()
                .filter(templateTag -> templateIds.contains(templateTag.getTemplate().getId()))
                .distinct()
                .map(TemplateTag::getTag)
                .toList();
    }

    @Override
    public List<TemplateTag> findAllByTemplateIdsIn(List<Long> templateIds) {
        return List.of();
    }
}
