package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.service.CategoryService;
import codezap.likes.service.LikedChecker;
import codezap.likes.service.LikesService;
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
    private final LikesService likesService;

    @Transactional
    public Long create(Member member, CreateTemplateRequest createTemplateRequest) {
        Category category = categoryService.fetchById(createTemplateRequest.categoryId());
        category.validateAuthorization(member);
        Template template = templateService.create(member, createTemplateRequest, category);
        tagService.createTags(template, createTemplateRequest.tags());
        sourceCodeService.createSourceCodes(template, createTemplateRequest.sourceCodes());
        SourceCode thumbnail = sourceCodeService.getByTemplateAndOrdinal(
                template,
                createTemplateRequest.thumbnailOrdinal());
        thumbnailService.createThumbnail(template, thumbnail);
        return template.getId();
    }

    public FindTemplateResponse findById(Long id) {
        Template template = templateService.getById(id);
        List<Tag> tags = tagService.findAllByTemplate(template);
        List<SourceCode> sourceCodes = sourceCodeService.findAllByTemplate(template);
        return FindTemplateResponse.of(template, sourceCodes, tags, false);
    }

    public FindTemplateResponse findByIdWithMember(Long id, Member member) {
        Template template = templateService.getById(id);
        List<Tag> tags = tagService.findAllByTemplate(template);
        List<SourceCode> sourceCodes = sourceCodeService.findAllByTemplate(template);
        boolean isLiked = likesService.isLiked(member, template);
        return FindTemplateResponse.of(template, sourceCodes, tags, isLiked);
    }

    public FindAllTemplatesResponse findAllBy(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        Page<Template> templates = templateService.findAllBy(memberId, keyword, categoryId, tagIds, pageable);
        return makeResponse(templates, (template) -> false);
    }

    public FindAllTemplatesResponse findAllByWithMember(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable,
            Member member
    ) {
        Page<Template> templates = templateService.findAllBy(memberId, keyword, categoryId, tagIds, pageable);
        return makeResponse(templates, (template -> likesService.isLiked(member, template)));
    }

    private FindAllTemplatesResponse makeResponse(Page<Template> page, LikedChecker likedChecker) {
        List<FindAllTemplateItemResponse> findAllTemplateByResponse = page.stream()
                .map(template -> FindAllTemplateItemResponse.of(
                        template,
                        tagService.findAllByTemplate(template),
                        thumbnailService.getByTemplate(template).getSourceCode(),
                        likedChecker.isLiked(template)))
                .toList();
        return new FindAllTemplatesResponse(
                page.getTotalPages(),
                page.getTotalElements(),
                findAllTemplateByResponse);
    }

    @Transactional
    public void update(Member member, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Category category = categoryService.fetchById(updateTemplateRequest.categoryId());
        category.validateAuthorization(member);
        Template template = templateService.update(member, templateId, updateTemplateRequest, category);
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
