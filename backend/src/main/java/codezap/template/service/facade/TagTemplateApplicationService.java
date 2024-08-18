package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;
import codezap.tag.service.TemplateTagService;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.tag.dto.response.FindAllTagsResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindAllTemplatesResponse.ItemResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.service.SourceCodeService;
import codezap.template.service.TemplateService;
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
        thumbnailService.createThumbnail(template, thumbnail);
        return template.getId();
    }

    public FindTemplateResponse getByMemberAndId(Member member, Long id) {
        Template template = templateService.getByMemberAndId(member, id);
        List<Tag> tags = templateTagService.getByTemplate(template);

        List<SourceCode> sourceCodes = sourceCodeService.getSourceCode(template);
        return FindTemplateResponse.of(template, sourceCodes, tags);
    }

    public FindAllTagsResponse getAllTagsByMemberId(Long memberId) {
        List<Template> template = templateService.getByMemberId(memberId);
        return templateTagService.findAllByTemplates(template);
    }

    public FindAllTemplatesResponse findAllBy(
            long memberId,
            String keyword,
            List<Long> tagIds,
            Pageable pageable
    ) {
        if (tagIds == null) {
            Page<Template> templates = templateService.findAllBy(memberId, keyword, pageable);
            return makeTemplatesResponse(templates);
        }

        List<Long> templateIds = templateTagService.getTemplateIdContainTagIds(tagIds);
        Page<Template> templates = templateService.findAllBy(memberId, keyword, templateIds, pageable);
        return makeTemplatesResponse(templates);
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
            return makeTemplatesResponse(templates);
        }

        List<Long> templateIds = templateTagService.getTemplateIdContainTagIds(tagIds);
        Page<Template> templates = templateService.findAllBy(memberId, keyword, categoryId, templateIds, pageable);
        return makeTemplatesResponse(templates);
    }

    private FindAllTemplatesResponse makeTemplatesResponse(Page<Template> page) {
        List<ItemResponse> itemResponses = page.stream()
                .map(template -> ItemResponse.of(
                        template,
                        templateTagService.getByTemplate(template),
                        thumbnailService.getByTemplate(template).getSourceCode())
                )
                .toList();
        return new FindAllTemplatesResponse(page.getTotalPages(), page.getTotalElements(), itemResponses);
    }

    @Transactional
    public void update(Member member, Category category, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Template template = templateService.updateTemplate(member, category, templateId, updateTemplateRequest);
        templateTagService.updateTags(updateTemplateRequest.tags(), template);
        Thumbnail thumbnail = thumbnailService.getByTemplate(template);
        sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);
    }

    @Transactional
    public void deleteByMemberAndIds(Member member, List<Long> ids) {
        templateService.deleteByMemberAndIds(member, ids);
        templateTagService.deleteByIds(ids);
        sourceCodeService.deleteByIds(ids);
        thumbnailService.deleteByIds(ids);
    }
}
