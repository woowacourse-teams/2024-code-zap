package codezap.template.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import codezap.template.domain.Template;

public interface TemplateRepository {

    Template fetchById(Long id);

    List<Template> findAll();

    List<Template> findByMemberId(Long id);

    Page<Template> searchBy(Long memberId, String keyword, Pageable pageable);

    Page<Template> searchBy(Long memberId, String keyword, List<Long> templateIds, Pageable pageable);

    Page<Template> searchBy(Long memberId, String keyword, Long categoryId, Pageable pageable);

    Page<Template> searchBy(Long memberId, String keyword, Long categoryId, List<Long> templateIds, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

    Template save(Template template);

    void deleteById(Long id);
}
