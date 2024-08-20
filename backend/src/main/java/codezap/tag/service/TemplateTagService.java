package codezap.tag.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import codezap.global.exception.CodeZapException;
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
public class TemplateTagService {
    private final TagRepository tagRepository;
    private final TemplateTagRepository templateTagRepository;

    public void createTags(Template template, List<String> tags) {
        tagRepository.saveAll(
                tags.stream()
                        .filter(tagName -> !tagRepository.existsByName(tagName))
                        .map(Tag::new)
                        .toList()
        );

        templateTagRepository.saveAll(
                tags.stream()
                        .map(tagRepository::fetchByName)
                        .map(tag -> new TemplateTag(template, tag))
                        .toList()
        );
    }

    public List<Tag> getByTemplate(Template template) {
        return templateTagRepository.findAllByTemplate(template).stream()
                .map(TemplateTag::getTag)
                .toList();
    }

    public List<Long> getTemplateIdContainTagIds(List<Long> tagIds) {
        if (tagIds.isEmpty()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "태그 ID가 0개입니다. 필터링 하지 않을 경우 null로 전달해주세요.");
        }
        tagIds.forEach(this::validateTagId);
        return templateTagRepository.findAllTemplateIdInTagIds(tagIds, tagIds.size());
    }

    private void validateTagId(Long tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + tagId + "에 해당하는 태그가 존재하지 않습니다.");
        }
    }

    public FindAllTagsResponse findAllByTemplates(List<Template> templates) {
        List<TemplateTag> templateTags = templateTagRepository.findByTemplateIn(templates);
        return new FindAllTagsResponse(
                templateTags.stream()
                        .map(TemplateTag::getTag)
                        .distinct()
                        .map(FindTagResponse::from)
                        .toList()
        );
    }

    public void updateTags(Template template, List<String> tags) {
        templateTagRepository.deleteAllByTemplateId(template.getId());
        tagRepository.saveAll(
                tags.stream()
                        .filter(tagName -> !tagRepository.existsByName(tagName))
                        .map(Tag::new)
                        .toList()
        );

        templateTagRepository.saveAll(
                tags.stream()
                        .map(tagRepository::fetchByName)
                        .map(tag -> new TemplateTag(template, tag))
                        .toList()
        );
    }

    public void deleteByIds(List<Long> templateIds) {
        templateIds.forEach(templateTagRepository::deleteAllByTemplateId);
    }
}
