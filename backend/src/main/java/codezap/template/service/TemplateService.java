package codezap.template.service;

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
import codezap.member.dto.MemberDto;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Snippet;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.ThumbnailSnippet;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSnippetRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllMyTemplatesResponse;
import codezap.template.dto.response.ExploreTemplatesResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindMyTemplateResponse;
import codezap.template.dto.response.FindAllTemplatesResponse.ItemResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.repository.SnippetRepository;
import codezap.template.repository.TagRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateTagRepository;
import codezap.template.repository.ThumbnailSnippetRepository;

@Service
public class TemplateService {

    public static final int FIRST_ORDINAL = 1;

    private final ThumbnailSnippetRepository thumbnailSnippetRepository;
    private final TemplateRepository templateRepository;
    private final SnippetRepository snippetRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TemplateTagRepository templateTagRepository;
    private final MemberRepository memberRepository;

    public TemplateService(ThumbnailSnippetRepository thumbnailSnippetRepository,
            TemplateRepository templateRepository, SnippetRepository snippetRepository,
            CategoryRepository categoryRepository, TagRepository tagRepository,
            TemplateTagRepository templateTagRepository,
            MemberRepository memberRepository
    ) {
        this.thumbnailSnippetRepository = thumbnailSnippetRepository;
        this.templateRepository = templateRepository;
        this.snippetRepository = snippetRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.templateTagRepository = templateTagRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long createTemplate(CreateTemplateRequest createTemplateRequest, MemberDto memberDto) {
        Member member = memberRepository.fetchById(memberDto.id());
        Category category = categoryRepository.fetchById(createTemplateRequest.categoryId());
        validateCategoryAuthorizeMember(category, member);
        Template template = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category)
        );
        createTags(createTemplateRequest, template);
        snippetRepository.saveAll(
                createTemplateRequest.snippets().stream()
                        .map(createSnippetRequest -> createSnippet(createSnippetRequest, template))
                        .toList()
        );

        Snippet thumbnailSnippet = snippetRepository.findByTemplateAndOrdinal(template, FIRST_ORDINAL)
                .orElseThrow(this::throwNotFoundSnippet);
        thumbnailSnippetRepository.save(new ThumbnailSnippet(template, thumbnailSnippet));
        return template.getId();
    }

    private void validateCategoryAuthorizeMember(Category category, Member member) {
        if (!category.getMember().equals(member)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "해당 카테고리에 대한 권한이 없는 유저입니다.");
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
                        .map(tag -> tagRepository.findByName(tag).orElseThrow(this::throwNotFoundTag))
                        .map(tag -> new TemplateTag(template, tag))
                        .toList()
        );
    }

    private Snippet createSnippet(CreateSnippetRequest createSnippetRequest, Template template) {
        return new Snippet(
                template, createSnippetRequest.filename(),
                createSnippetRequest.content(),
                createSnippetRequest.ordinal()
        );
    }

    public ExploreTemplatesResponse findAll() {
        return ExploreTemplatesResponse.from(thumbnailSnippetRepository.findAll());
    }

    public FindTemplateResponse findByIdAndMember(Long id, MemberDto memberDto) {
        Member member = memberRepository.fetchById(memberDto.id());
        Template template = templateRepository.fetchById(id);
        validateTemplateAuthorizeMember(template, member);

        List<Snippet> snippets = snippetRepository.findAllByTemplate(template);
        List<Tag> tags = templateTagRepository.findAllByTemplate(template).stream()
                .map(TemplateTag::getTag)
                .toList();
        return FindTemplateResponse.of(template, snippets, tags);
    }

    private void validateTemplateAuthorizeMember(Template template, Member member) {
        if (!template.getMember().equals(member)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "해당 템플릿에 대한 권한이 없는 유저입니다.");
        }
    }

    public FindAllTemplatesResponse findAllBy(
            //long memberId,
            PageRequest pageRequest,
            Long categoryId,
            List<String> tagNames
    ) {
        if (categoryId != null && tagNames != null) {
            List<Long> templateIds = filterTemplatesBy(tagNames);
            Page<Template> templatePage =
                    templateRepository.findByIdInAndCategoryId(pageRequest, templateIds, categoryId);
            long totalTemplatesCount = templateRepository.countByIdInAndCategoryId(templateIds, categoryId);
            return makeTemplatesResponseBy(totalTemplatesCount, templatePage);

        }
        if (categoryId != null && tagNames == null) {
            Page<Template> templatePage = templateRepository.findByCategoryId(pageRequest, categoryId);
            long totalTemplatesCount = templateRepository.countByCategoryId(categoryId);
            return makeTemplatesResponseBy(totalTemplatesCount, templatePage);
        }
        if (categoryId == null && tagNames != null) {
            List<Long> templateIds = filterTemplatesBy(tagNames);
            Page<Template> templatePage = templateRepository.findByIdIn(pageRequest, templateIds);
            long totalTemplatesCount = templateRepository.countByIdIn(templateIds);
            return makeTemplatesResponseBy(totalTemplatesCount, templatePage);
        }
        Page<Template> templatePage = templateRepository.findBy(pageRequest);
        long totalTemplatesCount = templateRepository.count();
        return makeTemplatesResponseBy(totalTemplatesCount, templatePage);
    }

    private List<Long> filterTemplatesBy(List<String> tagNames) {
        List<Tag> tags = tagRepository.findByNameIn(tagNames);
        List<TemplateTag> templateTags = templateTagRepository.findByTagIn(tags);
        return templateTags.stream()
                .map(TemplateTag::getTemplate)
                .map(Template::getId)
                .toList();
    }

    private FindAllTemplatesResponse makeTemplatesResponseBy(long totalTemplatesCount, Page<Template> page) {
        List<ItemResponse> itemResponses = page.stream()
                .map(template -> ItemResponse.of(template, getTemplateTags(template)))
                .toList();
        return new FindAllTemplatesResponse(page.getTotalPages(), totalTemplatesCount, itemResponses);
    }

    private List<Tag> getTemplateTags(Template template) {
        return templateTagRepository.findAllByTemplate(template).stream()
                .map(templateTag -> tagRepository.fetchById(templateTag.getTag().getId()))
                .toList();
    }

    @Transactional
    public void update(Long templateId, UpdateTemplateRequest updateTemplateRequest, MemberDto memberDto) {
        Member member = memberRepository.fetchById(memberDto.id());
        Category category = categoryRepository.fetchById(updateTemplateRequest.categoryId());
        validateCategoryAuthorizeMember(category, member);
        Template template = templateRepository.fetchById(templateId);
        validateTemplateAuthorizeMember(template, member);

        template.updateTemplate(updateTemplateRequest.title(), updateTemplateRequest.description(), category);
        updateSnippets(updateTemplateRequest, template);
        updateTags(updateTemplateRequest, template);
        validateSnippetsCount(updateTemplateRequest, template);
    }

    private void updateSnippets(UpdateTemplateRequest updateTemplateRequest, Template template) {
        updateTemplateRequest.updateSnippets().forEach(this::updateSnippet);
        snippetRepository.saveAll(
                updateTemplateRequest.createSnippets().stream()
                        .map(createSnippetRequest -> createSnippet(createSnippetRequest, template))
                        .toList()
        );

        ThumbnailSnippet thumbnailSnippet = thumbnailSnippetRepository.findByTemplate(template)
                .orElseThrow(this::throwNotFoundThumbnailSnippet);

        if (isThumbnailSnippetDeleted(updateTemplateRequest, thumbnailSnippet)) {
            updateThumbnailSnippet(template, thumbnailSnippet);
        }

        updateTemplateRequest.deleteSnippetIds().forEach(snippetRepository::deleteById);
    }

    private void updateSnippet(UpdateSnippetRequest updateSnippetRequest) {
        Snippet snippet = snippetRepository.fetchById(updateSnippetRequest.id());
        snippet.updateSnippet(updateSnippetRequest.filename(), updateSnippetRequest.content(),
                updateSnippetRequest.ordinal());
    }

    private boolean isThumbnailSnippetDeleted(
            UpdateTemplateRequest updateTemplateRequest,
            ThumbnailSnippet thumbnailSnippet
    ) {
        return updateTemplateRequest.deleteSnippetIds().contains(thumbnailSnippet.getId());
    }

    private void updateThumbnailSnippet(Template template, ThumbnailSnippet thumbnailSnippet) {
        List<Snippet> snippets = snippetRepository.findAllByTemplateAndOrdinal(template, FIRST_ORDINAL);
        snippets.stream()
                .filter(snippet -> !Objects.equals(thumbnailSnippet.getSnippet().getId(), snippet.getId()))
                .findFirst()
                .ifPresent(thumbnailSnippet::updateThumbnailSnippet);
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
                        .map(tag -> tagRepository.findByName(tag).orElseThrow(this::throwNotFoundTag))
                        .map(tag -> new TemplateTag(template, tag))
                        .toList()
        );
    }

    private void validateSnippetsCount(UpdateTemplateRequest updateTemplateRequest, Template template) {
        if (updateTemplateRequest.updateSnippets().size() + updateTemplateRequest.createSnippets().size()
                != snippetRepository.findAllByTemplate(template).size()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "스니펫의 정보가 정확하지 않습니다.");
        }
    }

    @Transactional
    public void deleteById(Long id, MemberDto memberDto) {
        Member member = memberRepository.fetchById(memberDto.id());
        Template template = templateRepository.fetchById(id);
        validateTemplateAuthorizeMember(template, member);

        thumbnailSnippetRepository.deleteByTemplateId(id);
        snippetRepository.deleteByTemplateId(id);
        templateTagRepository.deleteAllByTemplateId(id);
        templateRepository.deleteById(id);
    }

    public FindAllMyTemplatesResponse findContainTopic(Long memberId, String topic, Pageable pageable) {
        Page<Template> templates = getTemplates(memberId, topic, pageable);
        List<FindMyTemplateResponse> myTemplateResponses = templates.stream()
                .map(this::findByTemplate)
                .toList();

        return FindAllMyTemplatesResponse.of(myTemplateResponses, templates.getTotalPages());
    }

    private Page<Template> getTemplates(Long memberId, String topic, Pageable pageable) {
        String topicWithWildcards = "%" + topic + "%";
        Pageable newPageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        return templateRepository.searchBy(memberId, topicWithWildcards, newPageable);
    }

    private FindMyTemplateResponse findByTemplate(Template template) {
        List<Tag> tags = templateTagRepository.findAllByTemplate(template)
                .stream()
                .map(TemplateTag::getTag)
                .toList();

        return FindMyTemplateResponse.of(template, tags);
    }

    private CodeZapException throwNotFoundSnippet() {
        throw new CodeZapException(HttpStatus.NOT_FOUND, "해당하는 스니펫이 존재하지 않습니다.");
    }

    private CodeZapException throwNotFoundTag() {
        throw new CodeZapException(HttpStatus.NOT_FOUND, "해당하는 태그가 존재하지 않습니다.");
    }

    private CodeZapException throwNotFoundThumbnailSnippet() {
        throw new CodeZapException(HttpStatus.NOT_FOUND, "해당하는 썸네일 스니펫이 존재하지 않습니다.");
    }
}
