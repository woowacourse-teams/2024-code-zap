package codezap.template.service;

import java.util.List;

import org.springframework.stereotype.Service;

import codezap.representative_snippet.repository.RepresentativeSnippetRepository;
import codezap.snippet.domain.Snippet;
import codezap.snippet.repository.SnippetRepository;
import codezap.template.domain.Template;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateByIdResponse;
import codezap.template.repository.TemplateRepository;

@Service
public class TemplateService {

    private final RepresentativeSnippetRepository representativeSnippetRepository;
    private final TemplateRepository templateRepository;
    private final SnippetRepository snippetRepository;

    public TemplateService(RepresentativeSnippetRepository representativeSnippetRepository,
            TemplateRepository templateRepository,
            SnippetRepository snippetRepository
    ) {
        this.representativeSnippetRepository = representativeSnippetRepository;
        this.templateRepository = templateRepository;
        this.snippetRepository = snippetRepository;
    }

    public FindAllTemplatesResponse findAll() {
        return FindAllTemplatesResponse.from(representativeSnippetRepository.findAll());
    }

    public FindTemplateByIdResponse findById(Long id) {
        Template template = templateRepository.getById(id);
        List<Snippet> snippets = snippetRepository.findAllByTemplate(template);
        return FindTemplateByIdResponse.from(template, snippets);
    }

}
