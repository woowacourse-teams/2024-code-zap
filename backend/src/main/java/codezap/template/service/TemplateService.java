package codezap.template.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.extension.domain.Extension;
import codezap.extension.repository.ExtensionRepository;
import codezap.language.repository.LanguageRepository;
import codezap.member.repository.MemberRepository;
import codezap.representative_snippet.domain.RepresentativeSnippet;
import codezap.representative_snippet.repository.RepresentativeSnippetRepository;
import codezap.snippet.domain.Snippet;
import codezap.snippet.repository.SnippetRepository;
import codezap.template.domain.Template;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateByIdResponse;
import codezap.template.repository.TemplateRepository;

@Service
public class TemplateService {

    private final RepresentativeSnippetRepository representativeSnippetRepository;
    private final TemplateRepository templateRepository;
    private final SnippetRepository snippetRepository;
    private final ExtensionRepository extensionRepository;
    private final MemberRepository memberRepository;
    private final LanguageRepository languageRepository;

    public TemplateService(RepresentativeSnippetRepository representativeSnippetRepository,
            TemplateRepository templateRepository, SnippetRepository snippetRepository,
            ExtensionRepository extensionRepository, MemberRepository memberRepository,
            LanguageRepository languageRepository
    ) {
        this.representativeSnippetRepository = representativeSnippetRepository;
        this.templateRepository = templateRepository;
        this.snippetRepository = snippetRepository;
        this.extensionRepository = extensionRepository;
        this.memberRepository = memberRepository;
        this.languageRepository = languageRepository;
    }

    @Transactional
    public Long create(CreateTemplateRequest createTemplateRequest) {
        Template template = templateRepository.save(
                new Template(null, memberRepository.getById(1L), createTemplateRequest.title()));

        List<Snippet> snippets = createTemplateRequest.snippets().stream()
                .map(createSnippetRequest -> createSnippet(createSnippetRequest, template))
                .toList();

        RepresentativeSnippet representativeSnippet = representativeSnippetRepository.save(
                new RepresentativeSnippet(null, template, snippets.get(0)));
        return template.getId();
    }

    private Snippet createSnippet(CreateSnippetRequest createSnippetRequest, Template template) {
        String[] splitName = createSnippetRequest.filename().split("\\.");
        Extension extension = findExtensionOrCreate(splitName[splitName.length - 1]);

        return snippetRepository.save(
                new Snippet(null, template, extension, createSnippetRequest.filename(), createSnippetRequest.content(),
                        createSnippetRequest.ordinal()));
    }

    private Extension findExtensionOrCreate(String name) {
        return extensionRepository.findByName(name)
                .orElseGet(() -> extensionRepository.save(
                        new Extension(null, languageRepository.getByName("PlainText"), name)));
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
