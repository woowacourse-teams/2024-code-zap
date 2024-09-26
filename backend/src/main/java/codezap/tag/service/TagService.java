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
        List<String> existingTags = tagRepository.findNameByNamesIn(tagNames);
        templateTagRepository.saveAll(
                existingTags.stream()
                        .map(tagRepository::fetchByName)
                        .map(tag -> new TemplateTag(template, tag))
                        .toList()
        );

        List<Tag> newTags = tagRepository.saveAll(
                tagNames.stream()
                        .filter(tagName -> !existingTags.contains(tagName))
                        .map(Tag::new)
                        .toList()
        );
        templateTagRepository.saveAll(
                newTags.stream()
                        .map(tag -> new TemplateTag(template, tag))
                        .toList()
        );
    }

    public List<Tag> findAllByTemplate(Template template) {
        return templateTagRepository.findAllTagsByTemplate(template).stream()
                .toList();
    }

    public FindAllTagsResponse findAllByMemberId(Long memberId) {
        List<Long> templateTagIds = templateTagRepository.findAllTagIdDistinctByMemberId(memberId);
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
    public void deleteAllByTemplateIds(List<Long> templateIds) {
        templateTagRepository.deleteByTemplateIds(templateIds);
    }
}
