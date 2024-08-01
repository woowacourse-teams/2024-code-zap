package codezap.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

public interface TemplateTagRepository extends JpaRepository<TemplateTag, Long> {

    List<TemplateTag> findAllByTemplate(Template template);

    void deleteAllByTemplate(Template template);
}
