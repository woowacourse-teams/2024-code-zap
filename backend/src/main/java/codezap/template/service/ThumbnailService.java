package codezap.template.service;

import java.util.List;

import org.springframework.stereotype.Service;

import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.response.ExploreTemplatesResponse;
import codezap.template.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThumbnailService {
    private final ThumbnailRepository thumbnailRepository;

    public void createThumbnail(Template template, SourceCode thumbnail) {
        thumbnailRepository.save(new Thumbnail(template, thumbnail));
    }

    public Thumbnail getByTemplate(Template template) {
        return thumbnailRepository.fetchByTemplate(template);
    }

    public ExploreTemplatesResponse findAll() {
        return ExploreTemplatesResponse.from(thumbnailRepository.findAll());
    }

    public void deleteByIds(List<Long> templateIds) {
        for (Long id : templateIds) {
            thumbnailRepository.deleteByTemplateId(id);
        }
    }
}
