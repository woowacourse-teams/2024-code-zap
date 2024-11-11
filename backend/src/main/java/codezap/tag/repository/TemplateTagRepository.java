package codezap.tag.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import codezap.tag.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TemplateTagRepository {

    private final TemplateTagJpaRepository templateTagJpaRepository;
    private final TemplateTagQueryDSLRepository templateTagQueryDSLRepository;

    public List<TemplateTag> findAllByTemplate(Template template) {
        return templateTagQueryDSLRepository.findAllByTemplateId(template.getId());
    }

    public List<Tag> findAllTagsByTemplate(Template template) {
        return templateTagQueryDSLRepository.findAllTagsByTemplate(template);
    }

    public List<Tag> findAllTagDistinctByMemberId(Long memberId) {
        return templateTagQueryDSLRepository.findAllTagDistinctByMemberId(memberId);
    }

    public List<TemplateTag> findAllByTemplateId(Long templateId) {
        return templateTagQueryDSLRepository.findAllByTemplateId(templateId);
    }

    public List<TemplateTag> findAllByTemplateIdsIn(List<Long> templateIds) {
        return templateTagQueryDSLRepository.findAllByTemplateIdsIn(templateIds);
    }

    public TemplateTag save(TemplateTag templateTag) {
        return templateTagJpaRepository.save(templateTag);
    }

    public List<TemplateTag> saveAll(Iterable<TemplateTag> entities) {
        return templateTagJpaRepository.saveAll(entities);
    }

    public void deleteAllByTemplateId(Long templateId) {
        templateTagJpaRepository.deleteAllByTemplateId(templateId);
    }

    public void deleteAllByTemplateIds(List<Long> templateIds) {
        templateTagQueryDSLRepository.deleteAllByTemplateIds(templateIds);
    }
}
