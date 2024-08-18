package codezap.template.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.ExploreTemplatesResponse;
import codezap.template.dto.response.FindAllTagsResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindAllTemplatesResponse.ItemResponse;
import codezap.template.dto.response.FindTagResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TagRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateTagRepository;
import codezap.template.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final ThumbnailRepository thumbnailRepository;
    private final TemplateRepository templateRepository;
    private final SourceCodeRepository sourceCodeRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TemplateTagRepository templateTagRepository;

    @Transactional
    public Long createTemplate(Member member, CreateTemplateRequest createTemplateRequest) {
        Category category = categoryRepository.fetchById(createTemplateRequest.categoryId());
        validateCategoryAuthorizeMember(category, member);
        Template template = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category)
        );
        createTags(createTemplateRequest, template);
        sourceCodeRepository.saveAll(
                createTemplateRequest.sourceCodes().stream()
                        .map(createSourceCodeRequest -> createSourceCode(createSourceCodeRequest, template))
                        .toList()
        );

        SourceCode thumbnail = sourceCodeRepository.fetchByTemplateAndOrdinal(
                template, createTemplateRequest.thumbnailOrdinal());
        thumbnailRepository.save(new Thumbnail(template, thumbnail));
        return template.getId();
    }

    private void validateCategoryAuthorizeMember(Category category, Member member) {
        if (!category.getMember().equals(member)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "해당 카테고리에 대한 권한이 없습니다.");
        }
    }

    private void createTags(CreateTemplateRequest createTemplateRequest, Template template) {
        tagRepository.saveAll(
                createTemplateRequest.tags().stream()
                        .filter(tagName -> !tagRepository.existsByName(tagName))
                        .map(Tag::new)
                        .toList()
        );

        templateTagRepository.saveAll(
                createTemplateRequest.tags().stream()
                        .map(tagRepository::fetchByName)
                        .map(tag -> new TemplateTag(template, tag))
                        .toList()
        );
    }

    private SourceCode createSourceCode(CreateSourceCodeRequest createSourceCodeRequest, Template template) {
        return new SourceCode(
                template, createSourceCodeRequest.filename(),
                createSourceCodeRequest.content(),
                createSourceCodeRequest.ordinal()
        );
    }

    public ExploreTemplatesResponse findAll() {
        return ExploreTemplatesResponse.from(thumbnailRepository.findAll());
    }

    public FindTemplateResponse findByIdAndMember(Member member, Long id) {
        Template template = templateRepository.fetchById(id);
        validateTemplateAuthorizeMember(template, member);

        List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);
        List<Tag> tags = templateTagRepository.findAllByTemplate(template).stream()
                .map(TemplateTag::getTag)
                .toList();
        return FindTemplateResponse.of(template, sourceCodes, tags);
    }

    private void validateTemplateAuthorizeMember(Template template, Member member) {
        if (!template.getMember().equals(member)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "해당 템플릿에 대한 권한이 없습니다.");
        }
    }

    public FindAllTemplatesResponse findAllBy(
            long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        keyword = "%" + keyword + "%";
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

        if (categoryId != null && tagIds != null) {
            return getTemplatesResponseByCategoryAndTag(memberId, keyword, categoryId, tagIds, pageable);
        }
        if (categoryId != null) {
            return getTemplatesResponseCategory(memberId, keyword, categoryId, pageable);
        }
        if (tagIds != null) {
            return getTemplatesResponseByTag(memberId, keyword, tagIds, pageable);
        }
        return getTemplatesResponse(memberId, keyword, pageable);
    }

    private FindAllTemplatesResponse getTemplatesResponseByCategoryAndTag(
            long memberId, String keyword, Long categoryId, List<Long> tagIds, Pageable pageable
    ) {
        List<Long> templateIds = fetchTemplateIdContainsTagIds(tagIds);
        validateCategoryId(categoryId);
        Page<Template> templatePage =
                templateRepository.searchBy(memberId, keyword, categoryId, templateIds, pageable);
        return makeTemplatesResponseBy(templatePage);
    }

    private FindAllTemplatesResponse getTemplatesResponseCategory(
            long memberId, String keyword, Long categoryId, Pageable pageable
    ) {
        validateCategoryId(categoryId);
        Page<Template> templatePage = templateRepository.searchBy(memberId, keyword, categoryId, pageable);
        return makeTemplatesResponseBy(templatePage);
    }

    private void validateCategoryId(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)) {
            throw new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + categoryId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }
    }

    private FindAllTemplatesResponse getTemplatesResponseByTag(
            long memberId, String keyword, List<Long> tagIds, Pageable pageable
    ) {
        List<Long> templateIds = fetchTemplateIdContainsTagIds(tagIds);
        Page<Template> templatePage = templateRepository.searchBy(memberId, keyword, templateIds, pageable);
        return makeTemplatesResponseBy(templatePage);
    }

    private List<Long> fetchTemplateIdContainsTagIds(List<Long> tagIds) {
        if (tagIds.isEmpty()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "태그 ID가 0개입니다. 필터링 하지 않을 경우 null로 전달해주세요.");
        }
        for (Long id : tagIds) {
            validateTagId(id);
        }
        return templateTagRepository.findAllTemplateIdInTagIds(tagIds, tagIds.size());
    }

    private void validateTagId(Long tagId) {
        if(!tagRepository.existsById(tagId)) {
            throw new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + tagId + "에 해당하는 태그가 존재하지 않습니다.");
        }
    }

    private FindAllTemplatesResponse getTemplatesResponse(long memberId, String keyword, Pageable pageable) {
        Page<Template> templatePage = templateRepository.searchBy(memberId, keyword, pageable);
        return makeTemplatesResponseBy(templatePage);
    }

    private FindAllTemplatesResponse makeTemplatesResponseBy(Page<Template> page) {
        List<ItemResponse> itemResponses = page.stream()
                .map(template -> ItemResponse.of(template, getTemplateTags(template), getThumbnail(template)))
                .toList();
        return new FindAllTemplatesResponse(page.getTotalPages(), page.getTotalElements(), itemResponses);
    }

    private List<Tag> getTemplateTags(Template template) {
        return templateTagRepository.findAllByTemplate(template).stream()
                .map(templateTag -> tagRepository.fetchById(templateTag.getTag().getId()))
                .toList();
    }

    private SourceCode getThumbnail(Template template) {
        return thumbnailRepository.fetchByTemplate(template).getSourceCode();
    }

    @Transactional
    public void update(Member member, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Category category = categoryRepository.fetchById(updateTemplateRequest.categoryId());
        validateCategoryAuthorizeMember(category, member);
        Template template = templateRepository.fetchById(templateId);
        validateTemplateAuthorizeMember(template, member);

        template.updateTemplate(updateTemplateRequest.title(), updateTemplateRequest.description(), category);
        updateSourceCodes(updateTemplateRequest, template);
        updateTags(updateTemplateRequest, template);
        validateSourceCodesCount(updateTemplateRequest, template);
    }

    private void updateSourceCodes(UpdateTemplateRequest updateTemplateRequest, Template template) {
        updateTemplateRequest.updateSourceCodes().forEach(this::updateSourceCode);
        sourceCodeRepository.saveAll(
                updateTemplateRequest.createSourceCodes().stream()
                        .map(createSourceCodeRequest -> createSourceCode(createSourceCodeRequest, template))
                        .toList()
        );

        Thumbnail thumbnail = thumbnailRepository.fetchByTemplate(template);

        if (isThumbnailDeleted(updateTemplateRequest, thumbnail)) {
            updateThumbnail(template, thumbnail);
        }

        updateTemplateRequest.deleteSourceCodeIds().forEach(sourceCodeRepository::deleteById);
    }

    private void updateSourceCode(UpdateSourceCodeRequest updateSourceCodeRequest) {
        SourceCode sourceCode = sourceCodeRepository.fetchById(updateSourceCodeRequest.id());
        sourceCode.updateSourceCode(updateSourceCodeRequest.filename(), updateSourceCodeRequest.content(),
                updateSourceCodeRequest.ordinal());
    }

    private boolean isThumbnailDeleted(
            UpdateTemplateRequest updateTemplateRequest,
            Thumbnail thumbnail
    ) {
        return updateTemplateRequest.deleteSourceCodeIds().contains(thumbnail.getId());
    }

    private void updateThumbnail(Template template, Thumbnail thumbnail) {
        List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplateAndOrdinal(template,
                thumbnail.getSourceCode().getOrdinal());
        sourceCodes.stream()
                .filter(sourceCode -> !Objects.equals(thumbnail.getSourceCode().getId(), sourceCode.getId()))
                .findFirst()
                .ifPresent(thumbnail::updateThumbnail);
    }

    private void updateTags(UpdateTemplateRequest updateTemplateRequest, Template template) {
        templateTagRepository.deleteAllByTemplateId(template.getId());
        tagRepository.saveAll(
                updateTemplateRequest.tags().stream()
                        .filter(tagName -> !tagRepository.existsByName(tagName))
                        .map(Tag::new)
                        .toList()
        );

        templateTagRepository.saveAll(
                updateTemplateRequest.tags().stream()
                        .map(tagRepository::fetchByName)
                        .map(tag -> new TemplateTag(template, tag))
                        .toList()
        );
    }

    private void validateSourceCodesCount(UpdateTemplateRequest updateTemplateRequest, Template template) {
        if (updateTemplateRequest.updateSourceCodes().size() + updateTemplateRequest.createSourceCodes().size()
                != sourceCodeRepository.findAllByTemplate(template).size()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "소스 코드의 정보가 정확하지 않습니다.");
        }
    }

    public FindAllTagsResponse findAllTagsByMemberId(Long memberId) {
        List<Template> templates = templateRepository.findByMemberId(memberId);
        List<TemplateTag> templateTags = templateTagRepository.findByTemplateIn(templates);
        return new FindAllTagsResponse(
                templateTags.stream()
                        .map(TemplateTag::getTag)
                        .distinct()
                        .map(FindTagResponse::from)
                        .toList()
        );
    }

    @Transactional
    public void deleteByIds(Member member, List<Long> ids) {
        if (ids.size() != new HashSet<>(ids).size()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "삭제하고자 하는 템플릿 ID가 중복되었습니다.");
        }
        for (Long id : ids) {
            deleteById(member, id);
        }
    }

    private void deleteById(Member member, Long id) {
        Template template = templateRepository.fetchById(id);
        validateTemplateAuthorizeMember(template, member);

        thumbnailRepository.deleteByTemplateId(id);
        sourceCodeRepository.deleteByTemplateId(id);
        templateTagRepository.deleteAllByTemplateId(id);
        templateRepository.deleteById(id);
    }
}
