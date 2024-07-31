package codezap.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.TemplateTag;

public interface TemplateTagRepository extends JpaRepository<TemplateTag, Long> {
}
