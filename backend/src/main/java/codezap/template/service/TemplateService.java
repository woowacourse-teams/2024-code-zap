package codezap.template.service;

import org.springframework.stereotype.Service;

import codezap.representative_snippet.repository.RepresentativeSnippetRepository;
import codezap.template.dto.response.FindAllTemplatesResponse;

@Service
public class TemplateService {

    private final RepresentativeSnippetRepository representativeSnippetRepository;

    public TemplateService(RepresentativeSnippetRepository representativeSnippetRepository) {
        this.representativeSnippetRepository = representativeSnippetRepository;
    }

    public FindAllTemplatesResponse findAll() {
        return FindAllTemplatesResponse.from(representativeSnippetRepository.findAll());
    }
}
