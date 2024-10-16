package codezap.template.service.facade;

import java.util.List;

import jakarta.annotation.Nullable;

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
import codezap.template.domain.TemplateTag;
import codezap.template.domain.Thumbnail;
import codezap.template.domain.Visibility;
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

    public FindTemplateResponse findById(Long id, Member loginMember) {
        Template template = templateService.getById(id);
        List<Tag> tags = tagService.findAllByTemplate(template);
        List<SourceCode> sourceCodes = sourceCodeService.findAllByTemplate(template);
        boolean isLiked = likesService.isLiked(loginMember, template);
        return FindTemplateResponse.of(template, sourceCodes, tags, isLiked);
    }

    public FindAllTemplatesResponse findAllBy(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        Page<Template> templates = templateService.findAllBy(
                memberId, keyword, categoryId, tagIds, Visibility.PUBLIC, pageable
        );
        return makeResponse(templates, (template) -> false);
    }

    public FindAllTemplatesResponse findAllBy(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable,
            Member loginMember
    ) {
        Page<Template> templates = templateService.findAllBy(
                memberId, keyword, categoryId, tagIds, getDefaultVisibility(memberId, loginMember), pageable
        );
        return makeResponse(templates, (template -> likesService.isLiked(loginMember, template)));
    }

    @Nullable
    private Visibility getDefaultVisibility(Long memberId, Member loginMember) {
        if (memberId == null || !loginMember.matchId(memberId)) {
            return Visibility.PUBLIC;
        }
        return null;
    }

    private FindAllTemplatesResponse makeResponse(Page<Template> page, LikedChecker likedChecker) {
        List<Template> templates = page.getContent();
        List<FindAllTemplateItemResponse> findAllTemplateByResponse =
                getFindAllTemplateItemResponses(templates, likedChecker);

        return new FindAllTemplatesResponse(
                page.getTotalPages(),
                page.getTotalElements(),
                findAllTemplateByResponse);
    }

    private List<FindAllTemplateItemResponse> getFindAllTemplateItemResponses(
            List<Template> templates,
            LikedChecker likedChecker
    ) {
        List<TemplateTag> allTemplateTagsByTemplates = tagService.getAllTemplateTagsByTemplates(templates);
        List<Thumbnail> allThumbnailsByTemplates = thumbnailService.getAllByTemplates(templates);

        return templates.stream()
                .map(template -> FindAllTemplateItemResponse.of(
                        template,
                        getTagByTemplate(allTemplateTagsByTemplates, template),
                        getThumbnailSourceCodeByTemplate(allThumbnailsByTemplates, template),
                        likedChecker.isLiked(template)))
                .toList();
    }

    private List<Tag> getTagByTemplate(List<TemplateTag> templateTags, Template template) {
        return templateTags.stream()
                .filter(templateTag -> templateTag.hasTemplate(template))
                .map(TemplateTag::getTag)
                .toList();
    }

    private SourceCode getThumbnailSourceCodeByTemplate(List<Thumbnail> thumbnails, Template template) {
        return thumbnails.stream()
                .filter(thumbnail -> thumbnail.hasTemplate(template))
                .findFirst()
                .map(Thumbnail::getSourceCode)
                .orElseGet(() -> thumbnailService.getByTemplate(template).getSourceCode());
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
        sourceCodeService.deleteByTemplateIds(ids);
        tagService.deleteAllByTemplateIds(ids);
        likesService.deleteAllByTemplateIds(ids);
        templateService.deleteByMemberAndIds(member, ids);
    }
}
