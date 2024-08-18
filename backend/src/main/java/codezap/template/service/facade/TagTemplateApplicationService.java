package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.member.domain.Member;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTagsResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindAllTemplatesResponse.ItemResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.service.TemplateService;
import codezap.template.service.TemplateTagService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagTemplateApplicationService {
    private final TemplateTagService templateTagService;
    private final TemplateService templateService;

    @Transactional
    public Long createTemplate(Member member, CreateTemplateRequest createTemplateRequest) {
        Template template = templateService.createTemplate(member, createTemplateRequest);
        templateTagService.createTags(createTemplateRequest.tags(), template);
        return template.getId();
    }

    public FindAllTemplatesResponse findAllBy(
            long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        if (tagIds == null) {
            Page<Template> templates = templateService.findAllBy(memberId, keyword, categoryId, pageable);
            return makeTemplatesResponseBy(templates);
        }

        List<Long> templateIds = templateTagService.fetchTemplateIdsContainsTagIds(tagIds);
        Page<Template> templates = templateService.findAllBy(memberId, keyword, categoryId, templateIds, pageable);
        return makeTemplatesResponseBy(templates);
    }

    private FindAllTemplatesResponse makeTemplatesResponseBy(Page<Template> page) {
        List<ItemResponse> itemResponses = page.stream()
                .map(template -> ItemResponse.of(
                        template,
                        templateTagService.getByTemplate(template),
                        templateService.getThumbnail(template))
                )
                .toList();
        return new FindAllTemplatesResponse(page.getTotalPages(), page.getTotalElements(), itemResponses);
    }

    public FindTemplateResponse findByIdAndMember(Member member, Long id) {
        Template template = templateService.findByIdAndMember(member, id);
        List<Tag> tags = templateTagService.getByTemplate(template);
        return templateService.findSourceCode(template, tags);
    }

    public FindAllTagsResponse findAllTagsByMemberId(Long memberId) {
        List<Template> template = templateService.getByMemberId(memberId);
        return templateTagService.findAllTagsByMemberId(template);
    }

    @Transactional
    public void update(Member member, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Template template = templateService.update(member, templateId, updateTemplateRequest);
        templateTagService.updateTags(updateTemplateRequest.tags(), template);
    }

    @Transactional
    public void deleteByIds(Member member, List<Long> ids) {
        templateService.deleteByIds(member, ids);
        templateTagService.deleteByIds(ids);
    }
}
