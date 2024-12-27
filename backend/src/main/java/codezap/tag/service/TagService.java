package codezap.tag.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindAllTagsResponse;
import codezap.tag.dto.response.FindTagResponse;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final TemplateTagRepository templateTagRepository;

    @Transactional
    public void createTags(Template template, List<String> tagNames) {
        var existTags = tagRepository.findAllByNames(tagNames);
        var existNames = getExistTagNames(existTags);
        var newTags = getOnlyNewTags(existNames, tagNames);
        var savedNewTags = tagRepository.saveAll(newTags);
        existTags.addAll(savedNewTags);
        saveTemplateTags(template, existTags);
    }

    private List<String> getExistTagNames(List<Tag> existTags) {
        return existTags.stream()
                .map(Tag::getName)
                .toList();
    }

    private List<Tag> getOnlyNewTags(List<String> existNames, List<String> tagNames) {
        return tagNames.stream()
                .distinct()
                .filter(name -> !existNames.contains(name))
                .map(Tag::new)
                .toList();
    }

    private void saveTemplateTags(Template template, List<Tag> tags) {
        var templateTags = tags.stream()
                .map(tag -> new TemplateTag(template, tag))
                .toList();
        templateTagRepository.saveAll(templateTags);
    }

    public List<Tag> findAllByTemplate(Template template) {
        return templateTagRepository.findAllTagsByTemplate(template);
    }

    public List<TemplateTag> getAllTemplateTagsByTemplates(List<Template> templates) {
        var templateIds = templates.stream().map(Template::getId).toList();
        return templateTagRepository.findAllByTemplateIdsIn(templateIds);
    }

    public FindAllTagsResponse findAllByMemberId(Long memberId) {
        var tags = templateTagRepository.findAllTagDistinctByMemberId(memberId);
        return new FindAllTagsResponse(tags.stream()
                .map(FindTagResponse::from)
                .toList());
    }

    @Transactional
    public void updateTags(Template template, List<String> tags) {
        templateTagRepository.deleteAllByTemplateId(template.getId());
        createTags(template, tags);
    }

    @Transactional
    public void deleteAllByTemplateIds(List<Long> templateIds) {
        templateTagRepository.deleteAllByTemplateIds(templateIds);
    }
}
