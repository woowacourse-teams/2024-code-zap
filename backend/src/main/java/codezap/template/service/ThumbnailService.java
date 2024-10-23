package codezap.template.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.response.ExploreTemplatesResponse;
import codezap.template.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThumbnailService {

    private final ThumbnailRepository thumbnailRepository;

    @Transactional
    public void createThumbnail(Template template, SourceCode thumbnail) {
        thumbnailRepository.save(new Thumbnail(template, thumbnail));
    }

    public Thumbnail getByTemplate(Template template) {
        return thumbnailRepository.fetchByTemplate(template);
    }

    public List<Thumbnail> getAllByTemplates(List<Template> templates) {
        List<Long> templateIds = templates.stream()
                .map(Template::getId)
                .toList();

        return thumbnailRepository.findAllByTemplateIn(templateIds);
    }

    public ExploreTemplatesResponse findAll() {
        return ExploreTemplatesResponse.from(thumbnailRepository.findAll());
    }

    @Transactional
    public void deleteAllByTemplateIds(List<Long> templateIds) {
        thumbnailRepository.deleteAllByTemplateIds(templateIds);
    }
}
