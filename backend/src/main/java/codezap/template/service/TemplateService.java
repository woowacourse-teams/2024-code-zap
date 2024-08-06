package codezap.template.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.template.domain.Snippet;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.ThumbnailSnippet;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSnippetRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.repository.SnippetRepository;
import codezap.template.repository.TagRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateTagRepository;
import codezap.template.repository.ThumbnailSnippetRepository;

@Service
public class TemplateService {

    private static final int FIRST_ORDINAL = 1;

    private final ThumbnailSnippetRepository thumbnailSnippetRepository;
    private final TemplateRepository templateRepository;
    private final SnippetRepository snippetRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TemplateTagRepository templateTagRepository;

    public TemplateService(ThumbnailSnippetRepository thumbnailSnippetRepository,
            TemplateRepository templateRepository, SnippetRepository snippetRepository,
            CategoryRepository categoryRepository, TagRepository tagRepository,
            TemplateTagRepository templateTagRepository
    ) {
        this.thumbnailSnippetRepository = thumbnailSnippetRepository;
        this.templateRepository = templateRepository;
        this.snippetRepository = snippetRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.templateTagRepository = templateTagRepository;
    }

    @Transactional
    public Long createTemplate(CreateTemplateRequest createTemplateRequest) {
        Category category = categoryRepository.fetchById(createTemplateRequest.categoryId());
        Template template = templateRepository.save(
                new Template(createTemplateRequest.title(), createTemplateRequest.description(), category)
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

    public FindAllTemplatesResponse findAll() {
        return FindAllTemplatesResponse.from(thumbnailSnippetRepository.findAll());
    }

    public FindTemplateResponse findById(Long id) {
        Template template = templateRepository.fetchById(id);
        List<Snippet> snippets = snippetRepository.findAllByTemplate(template);
        List<Tag> tags = templateTagRepository.findAllByTemplate(template).stream()
                .map(TemplateTag::getTag)
                .toList();
        return FindTemplateResponse.of(template, snippets, tags);
    }

    @Transactional
    public void update(Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Category category = categoryRepository.fetchById(updateTemplateRequest.categoryId());
        Template template = templateRepository.fetchById(templateId);
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

    private static boolean isThumbnailSnippetDeleted(
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
    public void deleteById(Long id) {
        thumbnailSnippetRepository.deleteByTemplateId(id);
        snippetRepository.deleteByTemplateId(id);
        templateTagRepository.deleteAllByTemplateId(id);
        templateRepository.deleteById(id);
    }

    private CodeZapException throwNotFoundSnippet() {
        throw new CodeZapException(HttpStatus.NOT_FOUND, "해당하는 스니펫이 존재하지 않습니다.");
    }

    private CodeZapException throwNotFoundTag() {
        throw new CodeZapException(HttpStatus.BAD_REQUEST, "해당하는 태그가 존재하지 않습니다.");
    }

    private CodeZapException throwNotFoundThumbnailSnippet() {
        throw new CodeZapException(HttpStatus.BAD_REQUEST, "해당하는 썸네일 스니펫이 존재하지 않습니다.");
    }
}
