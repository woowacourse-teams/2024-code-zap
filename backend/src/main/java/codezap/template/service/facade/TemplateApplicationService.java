package codezap.template.service.facade;

import java.util.List;

import jakarta.annotation.Nullable;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.service.CategoryService;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.global.pagination.FixedPage;
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
@Transactional(readOnly = true)
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
        return makeTemplateResponse(id, template -> false, template -> false);
    }

    public FindTemplateResponse findById(Long id, Member loginMember) {
        return makeTemplateResponse(
                id,
                template -> template.matchMember(loginMember),
                template -> likesService.isLiked(loginMember, template)
        );
    }

    private FindTemplateResponse makeTemplateResponse(
            Long id,
            TemplateOwnershipChecker templateOwnershipChecker,
            LikedChecker likedChecker
    ) {
        Template template = templateService.getById(id);
        if (!templateOwnershipChecker.isOwner(template) && template.isPrivate()) {
            throw new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "해당 템플릿은 비공개 템플릿입니다.");
        }
        List<Tag> tags = tagService.findAllByTemplate(template);
        List<SourceCode> sourceCodes = sourceCodeService.findAllByTemplate(template);
        return FindTemplateResponse.of(template, sourceCodes, tags, likedChecker.isLiked(template));
    }

    public FindAllTemplatesResponse findAllBy(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        FixedPage<Template> templates = templateService.findAllBy(
                memberId, keyword, categoryId, tagIds, Visibility.PUBLIC, pageable
        );
        return makeAllTemplatesResponse(templates, (template) -> false);
    }

    public FindAllTemplatesResponse findAllBy(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable,
            Member loginMember
    ) {
        FixedPage<Template> templates = templateService.findAllBy(
                memberId, keyword, categoryId, tagIds, getVisibilityLevel(memberId, loginMember), pageable
        );
        return makeAllTemplatesResponse(templates, (template -> likesService.isLiked(loginMember, template)));
    }

    @Nullable
    private Visibility getVisibilityLevel(Long memberId, Member loginMember) {
        if (memberId == null || !loginMember.matchId(memberId)) {
            return Visibility.PUBLIC;
        }
        return null;
    }

    public FindAllTemplatesResponse findAllByLiked(Long memberId, Pageable pageable) {
        FixedPage<Template> likeTemplate = likesService.findAllByMemberId(memberId, pageable);
        return makeAllTemplatesResponse(likeTemplate, (template -> true));
    }

    private FindAllTemplatesResponse makeAllTemplatesResponse(FixedPage<Template> page, LikedChecker likedChecker) {
        List<Template> templates = page.contents();
        List<FindAllTemplateItemResponse> findAllTemplateByResponse =
                getFindAllTemplateItemResponses(templates, likedChecker);

        return new FindAllTemplatesResponse(
                page.nextPages(),
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
    public void deleteAllByMemberAndTemplateIds(Member member, List<Long> templateIds) {
        thumbnailService.deleteAllByTemplateIds(templateIds);
        sourceCodeService.deleteAllByTemplateIds(templateIds);
        tagService.deleteAllByTemplateIds(templateIds);
        likesService.deleteAllByTemplateIds(templateIds);
        templateService.deleteByMemberAndIds(member, templateIds);
    }
}
