package codezap.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.TemplateTag;

public interface TemplateTagJpaRepository extends JpaRepository<TemplateTag, Long> {
    void deleteAllByTemplateId(Long templateId);
}
