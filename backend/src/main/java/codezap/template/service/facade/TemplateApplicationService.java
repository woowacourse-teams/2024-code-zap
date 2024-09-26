package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.service.CategoryService;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;
import codezap.tag.service.TagService;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplateItemResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.service.SourceCodeService;
import codezap.template.service.TemplateService;
import codezap.template.service.ThumbnailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateApplicationService {

    private final TemplateService templateService;
    private final SourceCodeService sourceCodeService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final ThumbnailService thumbnailService;

    @Transactional
    public Long createTemplate(Member member, CreateTemplateRequest createTemplateRequest) {
        Category category = categoryService.fetchById(createTemplateRequest.categoryId());
        category.validateAuthorization(member);
        Template template = templateService.createTemplate(member, createTemplateRequest, category);
        tagService.createTags(template, createTemplateRequest.tags());
        sourceCodeService.createSourceCodes(template, createTemplateRequest.sourceCodes());
        SourceCode thumbnail = sourceCodeService.getByTemplateAndOrdinal(
                template,
                createTemplateRequest.thumbnailOrdinal());
        thumbnailService.createThumbnail(template, thumbnail);
        return template.getId();
    }

    public FindTemplateResponse findTemplateById(Long id) {
        Template template = templateService.getById(id);
        List<Tag> tags = tagService.findAllByTemplate(template);
        List<SourceCode> sourceCodes = sourceCodeService.findSourceCodesByTemplate(template);
        return FindTemplateResponse.of(template, sourceCodes, tags, 0L, false);
    }

    public FindAllTemplatesResponse findAllTemplatesBy(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        Page<Template> templates = templateService.findAll(memberId, keyword, categoryId, tagIds, pageable);
        List<FindAllTemplateItemResponse> findAllTemplateByResponse = templates.stream()
                .map(template -> FindAllTemplateItemResponse.of(
                        template,
                        tagService.findAllByTemplate(template),
                        thumbnailService.getByTemplate(template).getSourceCode(),
                        0L,
                        false))
                .toList();
        return new FindAllTemplatesResponse(
                templates.getTotalPages(),
                templates.getTotalElements(),
                findAllTemplateByResponse);
    }

    @Transactional
    public void update(Member member, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Category category = categoryService.fetchById(updateTemplateRequest.categoryId());
        category.validateAuthorization(member);
        Template template = templateService.updateTemplate(member, templateId, updateTemplateRequest, category);
        tagService.updateTags(template, updateTemplateRequest.tags());
        Thumbnail thumbnail = thumbnailService.getByTemplate(template);
        sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);
    }

    @Transactional
    public void deleteByMemberAndIds(Member member, List<Long> ids) {
        thumbnailService.deleteByTemplateIds(ids);
        sourceCodeService.deleteByIds(ids);
        tagService.deleteByIds(ids);
        templateService.deleteByMemberAndIds(member, ids);
    }
}
