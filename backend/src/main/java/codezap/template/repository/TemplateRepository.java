package codezap.template.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import codezap.template.domain.Template;

public interface TemplateRepository {

    Template fetchById(Long id);

    List<Template> findByMemberId(Long id);

    Page<Template> findAll(Specification<Template> specification, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

    Template save(Template template);

    void deleteById(Long id);
}
