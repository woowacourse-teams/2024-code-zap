package codezap.template.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.template.domain.Snippet;
import codezap.template.domain.Template;
import codezap.template.domain.ThumbnailSnippet;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSnippetRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateByIdResponse;
import codezap.template.repository.SnippetRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailSnippetRepository;

@Service
public class TemplateService {

    public static final int FIRST_ORDINAL = 1;

    private final ThumbnailSnippetRepository thumbnailSnippetRepository;
    private final TemplateRepository templateRepository;
    private final SnippetRepository snippetRepository;

    public TemplateService(ThumbnailSnippetRepository thumbnailSnippetRepository,
            TemplateRepository templateRepository, SnippetRepository snippetRepository
    ) {
        this.thumbnailSnippetRepository = thumbnailSnippetRepository;
        this.templateRepository = templateRepository;
        this.snippetRepository = snippetRepository;
    }

    @Transactional
    public Long create(CreateTemplateRequest createTemplateRequest) {
        Template template = templateRepository.save(
                new Template(createTemplateRequest.title()));

        createTemplateRequest.snippets()
                .forEach(createSnippetRequest -> createSnippet(createSnippetRequest, template));

        Snippet thumbnailSnippet = snippetRepository.findByTemplateAndOrdinal(template, 1);
        thumbnailSnippetRepository.save(new ThumbnailSnippet(template, thumbnailSnippet));
        return template.getId();
    }

    public FindAllTemplatesResponse findAll() {
        return FindAllTemplatesResponse.from(thumbnailSnippetRepository.findAll());
    }

    public FindTemplateByIdResponse findById(Long id) {
        Template template = templateRepository.fetchById(id);
        List<Snippet> snippets = snippetRepository.findAllByTemplate(template);
        return FindTemplateByIdResponse.of(template, snippets);
    }

    @Transactional
    public void update(Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Template template = templateRepository.fetchById(templateId);
        template.updateTitle(updateTemplateRequest.title());

        ThumbnailSnippet thumbnailSnippet = thumbnailSnippetRepository.findByTemplate(template);

        updateTemplateRequest.updateSnippets()
                .forEach(this::updateSnippet);
        updateTemplateRequest.createSnippets()
                .forEach(createSnippetRequest -> createSnippet(createSnippetRequest, template));

        List<Snippet> snippets = snippetRepository.findAllByTemplateAndOrdinal(template, FIRST_ORDINAL);

        if (updateTemplateRequest.deleteSnippetIds().contains(thumbnailSnippet.getId())) {
            snippets.stream()
                    .filter(snippet -> thumbnailSnippet.getSnippet().getId() != snippet.getId())
                    .findFirst()
                    .ifPresent(thumbnailSnippet::updateThumbnailSnippet);
        }

        updateTemplateRequest.deleteSnippetIds()
                .forEach(snippetRepository::deleteById);
    }

    private void createSnippet(CreateSnippetRequest createSnippetRequest, Template template) {
        snippetRepository.save(
                new Snippet(
                        template, createSnippetRequest.filename(),
                        createSnippetRequest.content(),
                        createSnippetRequest.ordinal()
                )
        );
    }

    private void updateSnippet(UpdateSnippetRequest updateSnippetRequest) {
        Snippet snippet = snippetRepository.fetchById(updateSnippetRequest.id());
        snippet.updateSnippet(updateSnippetRequest.filename(), updateSnippetRequest.content(),
                updateSnippetRequest.ordinal());
    }
}
