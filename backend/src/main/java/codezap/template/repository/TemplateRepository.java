package codezap.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.Template;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}
