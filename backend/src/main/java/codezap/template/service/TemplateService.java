package codezap.template.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.template.domain.Snippet;
import codezap.template.domain.Template;
import codezap.template.domain.ThumbnailSnippet;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateByIdResponse;
import codezap.template.repository.SnippetRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailSnippetRepository;

@Service
public class TemplateService {

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
                new Template(null, createTemplateRequest.title()));

        List<Snippet> snippets = createTemplateRequest.snippets().stream()
                .map(createSnippetRequest -> createSnippet(createSnippetRequest, template))
                .toList();

        thumbnailSnippetRepository.save(new ThumbnailSnippet(null, template, snippets.get(0)));
        return template.getId();
    }

    private Snippet createSnippet(CreateSnippetRequest createSnippetRequest, Template template) {
        return snippetRepository.save(
                new Snippet(null, template, createSnippetRequest.filename(), createSnippetRequest.content(),
                        createSnippetRequest.ordinal()));
    }

    public FindAllTemplatesResponse findAll() {
        return FindAllTemplatesResponse.from(thumbnailSnippetRepository.findAll());
    }

    public FindTemplateByIdResponse findById(Long id) {
        Template template = templateRepository.getById(id);
        List<Snippet> snippets = snippetRepository.findAllByTemplate(template);
        return FindTemplateByIdResponse.of(template, snippets);
    }
}
