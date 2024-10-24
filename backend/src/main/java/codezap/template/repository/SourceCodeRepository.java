package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class SourceCodeRepository {

    private final SourceCodeJpaRepository sourceCodeJpaRepository;
    private final SourceCodeQueryDSLRepository sourceCodeQueryDSLRepository;

    public SourceCode fetchById(Long id) {
        return sourceCodeJpaRepository.findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 소스 코드가 존재하지 않습니다."));
    }

    public SourceCode fetchByTemplateAndOrdinal(Template template, int ordinal) {
        return sourceCodeJpaRepository.findByTemplateAndOrdinal(template, ordinal)
                .orElseThrow(() -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "템플릿에 " + ordinal + "번째 소스 코드가 존재하지 않습니다."));
    }

    public List<SourceCode> findAllByTemplate(Template template) {
        return sourceCodeJpaRepository.findAllByTemplate(template);
    }

    public List<SourceCode> findAllByTemplateAndOrdinal(Template template, int ordinal) {
        return sourceCodeJpaRepository.findAllByTemplateAndOrdinal(template, ordinal);
    }

    public int countByTemplate(Template template) {
        return sourceCodeJpaRepository.countByTemplate(template);
    }

    public SourceCode save(SourceCode sourceCode) {
        return sourceCodeJpaRepository.save(sourceCode);
    }

    public List<SourceCode> saveAll(Iterable<SourceCode> entities) {
        return sourceCodeJpaRepository.saveAll(entities);
    }

    public void deleteById(Long id) {
        sourceCodeJpaRepository.deleteById(id);
    }

    public void deleteAllByTemplateIds(List<Long> templateIds) {
        sourceCodeQueryDSLRepository.deleteAllByTemplateIds(templateIds);
    }
}
