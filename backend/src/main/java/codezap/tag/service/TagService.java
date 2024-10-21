package codezap.tag.service;

import java.util.ArrayList;
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
        List<Tag> existingTags = new ArrayList<>(tagRepository.findAllByNames(tagNames));
        List<String> existNames = existingTags.stream()
                .map(Tag::getName)
                .toList();

        List<Tag> newTags = tagRepository.saveAll(
                tagNames.stream()
                        .filter(name -> !existNames.contains(name))
                        .map(Tag::new)
                        .toList()
        );
        existingTags.addAll(newTags);

        for (Tag existingTag : existingTags) {
            templateTagRepository.save(new TemplateTag(template, existingTag));
        }
    }

    public List<Tag> findAllByTemplate(Template template) {
        return templateTagRepository.findAllTagsByTemplate(template);
    }

    public List<Tag> findAllByTemplateId(Long templateId) {
        return templateTagRepository.findAllByTemplateId(templateId).stream()
                .map(TemplateTag::getTag)
                .toList();
    }

    public List<TemplateTag> getAllTemplateTagsByTemplates(List<Template> templates) {
        List<Long> templateIds = templates.stream().map(Template::getId).toList();
        return templateTagRepository.findAllByTemplateIdsIn(templateIds);
    }

    public FindAllTagsResponse findAllByMemberId(Long memberId) {
        List<Tag> tags = templateTagRepository.findAllTagDistinctByMemberId(memberId);
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
