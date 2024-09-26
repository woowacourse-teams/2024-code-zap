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
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TemplateRepository templateRepository;
    private final TemplateTagRepository templateTagRepository;

    @Transactional
    public void createTags(Template template, List<String> tagNames) {
        List<Tag> existingTags = new ArrayList<>(tagRepository.findByNameIn(tagNames));
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
        return templateTagRepository.findAllByTemplate(template).stream()
                .map(TemplateTag::getTag)
                .toList();
    }

    public FindAllTagsResponse findAllByMemberId(Long memberId) {
        List<Template> templates = templateRepository.findByMemberId(memberId);
        List<Long> templateIds = templates.stream().map(Template::getId).toList();
        List<Long> templateTagIds = templateTagRepository.findDistinctByTemplateIn(templateIds);
        return new FindAllTagsResponse(templateTagIds.stream()
                .map(id -> FindTagResponse.from(tagRepository.fetchById(id)))
                .toList());
    }

    @Transactional
    public void updateTags(Template template, List<String> tags) {
        templateTagRepository.deleteAllByTemplateId(template.getId());
        createTags(template, tags);
    }

    @Transactional
    public void deleteByIds(List<Long> templateIds) {
        templateIds.forEach(templateTagRepository::deleteAllByTemplateId);
    }
}
