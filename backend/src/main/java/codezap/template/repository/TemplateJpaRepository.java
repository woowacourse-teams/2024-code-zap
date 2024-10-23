package codezap.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.Template;

@SuppressWarnings("unused")
public interface TemplateJpaRepository extends JpaRepository<Template, Long> {

    List<Template> findByMemberId(Long id);

    boolean existsByCategoryId(Long categoryId);
}
