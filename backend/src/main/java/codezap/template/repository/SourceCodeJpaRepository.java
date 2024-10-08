package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;

@SuppressWarnings("unused")
public interface SourceCodeJpaRepository extends SourceCodeRepository, JpaRepository<SourceCode, Long> {
    default SourceCode fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 소스 코드가 존재하지 않습니다."));
    }

    List<SourceCode> findAllByTemplate(Template template);

    default SourceCode fetchByTemplateAndOrdinal(Template template, int ordinal) {
        return findByTemplateAndOrdinal(template, ordinal)
                .orElseThrow(
                        () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND,
                                "템플릿에 " + ordinal + "번째 소스 코드가 존재하지 않습니다."));
    }

    Optional<SourceCode> findByTemplateAndOrdinal(Template template, int ordinal);

    List<SourceCode> findAllByTemplateAndOrdinal(Template template, int ordinal);

    int countByTemplate(Template template);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM SourceCode s WHERE s.template.id in :templateIds")
    void deleteByTemplateIds(List<Long> templateIds);
}
