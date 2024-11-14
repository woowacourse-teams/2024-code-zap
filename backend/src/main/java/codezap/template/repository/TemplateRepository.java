package codezap.template.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.global.pagination.FixedPage;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TemplateRepository {

    private final TemplateJpaRepository templateJpaRepository;
    private final TemplateQueryDSLRepository templateQueryDSLRepository;

    public Template fetchById(Long id) {
        return templateJpaRepository.findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 템플릿이 존재하지 않습니다."));
    }

    public List<Template> findByMemberId(Long id) {
        return templateJpaRepository.findByMemberId(id);
    }

    public FixedPage<Template> findAll(
            Long memberId, String keyword, Long categoryId, List<Long> tagIds, Visibility visibility, Pageable pageable
    ) {
        return templateQueryDSLRepository.findTemplates(memberId, keyword, categoryId, tagIds, visibility, pageable);
    }

    public FixedPage<Template> findAllLikedByMemberId(Long memberId, Pageable pageable) {
        return templateQueryDSLRepository.findAllLikedByMemberId(memberId, pageable);
    }

    public boolean existsByCategoryId(Long categoryId) {
        return templateJpaRepository.existsByCategoryId(categoryId);
    }

    public Template save(Template template) {
        return templateJpaRepository.save(template);
    }

    public void deleteById(Long id) {
        templateJpaRepository.deleteById(id);
    }
}
