package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.likes.service.LikeChecker;
import codezap.likes.service.LikesService;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindAllTagsResponse;
import codezap.tag.service.TemplateTagService;
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
    private final TemplateTagService templateTagService;
    private final TemplateService templateService;
    private final ThumbnailService thumbnailService;
    private final SourceCodeService sourceCodeService;
    private final LikesService likesService;

    @Transactional
    public Long createTemplate(Member member, Category category, CreateTemplateRequest createTemplateRequest) {
        Template template = templateService.createTemplate(member, createTemplateRequest, category);
        templateTagService.createTags(template, createTemplateRequest.tags());
        sourceCodeService.createSourceCodes(template, createTemplateRequest.sourceCodes());
        SourceCode thumbnail = sourceCodeService.getByTemplateAndOrdinal(
                template,
                createTemplateRequest.thumbnailOrdinal()
        );
        thumbnailService.createThumbnail(template, thumbnail);
        return template.getId();
    }

    public FindTemplateResponse getById(Long id) {
        Template template = templateService.getById(id);
        List<Tag> tags = templateTagService.getByTemplate(template);

        List<SourceCode> sourceCodes = sourceCodeService.findSourceCodesByTemplate(template);

        return FindTemplateResponse.of(template, sourceCodes, tags, false);
    }

    public FindTemplateResponse getByIdWithMember(Long id, Member member) {
        Template template = templateService.getById(id);
        List<Tag> tags = templateTagService.getByTemplate(template);

        List<SourceCode> sourceCodes = sourceCodeService.findSourceCodesByTemplate(template);

        return FindTemplateResponse.of(template, sourceCodes, tags, likesService.isLiked(member, template));
    }

    public FindAllTagsResponse getAllTagsByMemberId(Long memberId) {
        List<Template> template = templateService.getByMemberId(memberId);
        return templateTagService.findAllByTemplates(template);
    }

    public FindAllTemplatesResponse findAllBy(
            Long memberId, String keyword, Long categoryId, List<Long> tagIds, Pageable pageable
    ) {
        Page<Template> templates = templateService.findAll(memberId, keyword, categoryId, tagIds, pageable);
        return makeTemplatesResponse(templates, (template) -> false);
    }

    public FindAllTemplatesResponse findAllByWithMember(
            Long memberId, String keyword, Long categoryId, List<Long> tagIds, Pageable pageable, Member loginMember
    ) {
        Page<Template> templates = templateService.findAll(memberId, keyword, categoryId, tagIds, pageable);
        return makeTemplatesResponse(templates, (template -> likesService.isLiked(loginMember, template)));
    }

    private FindAllTemplatesResponse makeTemplatesResponse(Page<Template> page, LikeChecker likeChecker) {
        List<FindAllTemplateItemResponse> findTemplateByAllResponse = page.stream()
                .map(template -> FindAllTemplateItemResponse.of(
                        template,
                        templateTagService.getByTemplate(template),
                        thumbnailService.getByTemplate(template).getSourceCode(),
                        likeChecker.isLike(template))
                )
                .toList();
        return new FindAllTemplatesResponse(page.getTotalPages(), page.getTotalElements(), findTemplateByAllResponse);
    }

    @Transactional
    public void update(Member member, Long templateId, UpdateTemplateRequest updateTemplateRequest, Category category) {
        Template template = templateService.updateTemplate(member, templateId, updateTemplateRequest, category);
        templateTagService.updateTags(template, updateTemplateRequest.tags());
        Thumbnail thumbnail = thumbnailService.getByTemplate(template);
        sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);
    }

    @Transactional
    public void deleteByMemberAndIds(Member member, List<Long> ids) {
        thumbnailService.deleteByTemplateIds(ids);
        sourceCodeService.deleteByIds(ids);
        templateTagService.deleteByIds(ids);
        templateService.deleteByMemberAndIds(member, ids);
    }
}
