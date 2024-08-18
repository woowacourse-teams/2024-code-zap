package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.member.domain.Member;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTagsResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindAllTemplatesResponse.ItemResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.service.SourceCodeService;
import codezap.template.service.TemplateService;
import codezap.template.service.TemplateTagService;
import codezap.template.service.ThumbnailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagTemplateApplicationService {
    private final TemplateTagService templateTagService;
    private final TemplateService templateService;
    private final ThumbnailService thumbnailService;
    private final SourceCodeService sourceCodeService;

    @Transactional
    public Long createTemplate(Member member, Category category, CreateTemplateRequest createTemplateRequest) {
        Template template = templateService.createTemplate(member, category, createTemplateRequest);
        templateTagService.createTags(createTemplateRequest.tags(), template);
        SourceCode thumbnail = sourceCodeService.createSourceCodes(
                createTemplateRequest.sourceCodes(),
                createTemplateRequest.thumbnailOrdinal(),
                template
        );
        thumbnailService.save(template, thumbnail);
        return template.getId();
    }

    public FindAllTemplatesResponse findAllBy(
            long memberId,
            String keyword,
            List<Long> tagIds,
            Pageable pageable
    ) {
        if (tagIds == null) {
            Page<Template> templates = templateService.findAllBy(memberId, keyword, pageable);
            return makeTemplatesResponseBy(templates);
        }

        List<Long> templateIds = templateTagService.fetchTemplateIdsContainsTagIds(tagIds);
        Page<Template> templates = templateService.findAllBy(memberId, keyword, templateIds, pageable);
        return makeTemplatesResponseBy(templates);
    }

    public FindAllTemplatesResponse findAllBy(
            long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

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
                        thumbnailService.getThumbnail(template))
                )
                .toList();
        return new FindAllTemplatesResponse(page.getTotalPages(), page.getTotalElements(), itemResponses);
    }

    public FindTemplateResponse findByIdAndMember(Member member, Long id) {
        Template template = templateService.findByIdAndMember(member, id);
        List<Tag> tags = templateTagService.getByTemplate(template);
        return sourceCodeService.findSourceCode(template, tags);
    }

    public FindAllTagsResponse findAllTagsByMemberId(Long memberId) {
        List<Template> template = templateService.getByMemberId(memberId);
        return templateTagService.findAllTagsByMemberId(template);
    }

    @Transactional
    public void update(Member member, Category category, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Template template = templateService.update(member, category, templateId, updateTemplateRequest);
        templateTagService.updateTags(updateTemplateRequest.tags(), template);
        Thumbnail thumbnail = thumbnailService.fetchByTemplate(template);
        sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);
    }

    @Transactional
    public void deleteByIds(Member member, List<Long> ids) {
        templateService.deleteByIds(member, ids);
        templateTagService.deleteByIds(ids);
        sourceCodeService.deleteByIds(ids);
        thumbnailService.deleteByIds(ids);
    }
}
