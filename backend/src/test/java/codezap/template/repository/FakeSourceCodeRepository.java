package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;

public class FakeSourceCodeRepository implements SourceCodeRepository {

    private final AtomicLong idCounter = new AtomicLong(1);

    private final List<SourceCode> sourceCodes;

    public FakeSourceCodeRepository() {
        this.sourceCodes = new ArrayList<>();
    }

    @Override
    public SourceCode fetchById(Long id) {
        return sourceCodes.stream()
                .filter(sourceCode -> Objects.equals(sourceCode.getId(), id))
                .findFirst()
                .orElseThrow(
                        () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 소스 코드가 존재하지 않습니다."));
    }

    @Override
    public List<SourceCode> findAllByTemplate(Template template) {
        return sourceCodes.stream()
                .filter(sourceCode -> Objects.equals(sourceCode.getTemplate(), template))
                .toList();
    }

    @Override
    public SourceCode fetchByTemplateAndOrdinal(Template template, int ordinal) {
        return findByTemplateAndOrdinal(template, ordinal)
                .orElseThrow(
                        () -> new CodeZapException(HttpStatus.NOT_FOUND, "템플릿에 " + ordinal + "번째 소스 코드가 존재하지 않습니다."));
    }

    @Override
    public Optional<SourceCode> findByTemplateAndOrdinal(Template template, int ordinal) {
        return sourceCodes.stream()
                .filter(sourceCode ->
                        Objects.equals(sourceCode.getTemplate(), template) &&
                                Objects.equals(sourceCode.getOrdinal(), ordinal)
                ).findFirst();
    }

    @Override
    public List<SourceCode> findAllByTemplateAndOrdinal(Template template, int ordinal) {
        return sourceCodes.stream()
                .filter(sourceCode ->
                        Objects.equals(sourceCode.getTemplate(), template) &&
                                Objects.equals(sourceCode.getOrdinal(), ordinal)
                ).toList();
    }

    @Override
    public int countByTemplate(Template template) {
        return (int) sourceCodes.stream()
                .filter(sourceCode -> Objects.equals(sourceCode.getTemplate(), template))
                .count();
    }

    @Override
    public SourceCode save(SourceCode entity) {
        var saved = new SourceCode(
                getOrGenerateId(entity),
                entity.getTemplate(),
                entity.getFilename(),
                entity.getContent(),
                entity.getOrdinal()
        );
        sourceCodes.removeIf(sourceCode -> Objects.equals(sourceCode.getId(), entity.getId()));
        sourceCodes.add(saved);
        return saved;
    }

    @Override
    public <S extends SourceCode> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
        return (List<S>) sourceCodes;
    }

    @Override
    public void deleteById(Long id) {
        sourceCodes.removeIf(sourceCode -> Objects.equals(sourceCode.getId(), id));
    }

    @Override
    public void deleteByTemplateIds(List<Long> templateIds) {
        templateIds.forEach(id ->
                sourceCodes.removeIf(sourceCode -> Objects.equals(sourceCode.getId(), id)));
    }

    private long getOrGenerateId(SourceCode entity) {
        if (existsById(entity.getId())) {
            return entity.getId();
        }
        return idCounter.getAndIncrement();
    }

    public boolean existsById(Long id) {
        return sourceCodes.stream().anyMatch(sourceCode -> Objects.equals(sourceCode.getId(), id));
    }
}
