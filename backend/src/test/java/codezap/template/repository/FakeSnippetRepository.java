package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Snippet;
import codezap.template.domain.Template;

public class FakeSnippetRepository implements SnippetRepository {

    private final AtomicLong idCounter = new AtomicLong(1);

    private final List<Snippet> snippets;

    public FakeSnippetRepository() {
        this.snippets = new ArrayList<>();
    }

    @Override
    public Snippet fetchById(Long id) {
        return snippets.stream()
                .filter(snippet -> Objects.equals(snippet.getId(), id))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 스니펫이 존재하지 않습니다."));
    }

    @Override
    public List<Snippet> findAll() {
        return snippets;
    }

    @Override
    public List<Snippet> findAllByTemplate(Template template) {
        return snippets.stream()
                .filter(snippet -> Objects.equals(snippet.getTemplate(), template))
                .toList();
    }

    @Override
    public Optional<Snippet> findByTemplateAndOrdinal(Template template, int ordinal) {
        return snippets.stream()
                .filter(snippet ->
                        Objects.equals(snippet.getTemplate(), template) &&
                                Objects.equals(snippet.getOrdinal(), ordinal)
                ).findFirst();
    }

    @Override
    public List<Snippet> findAllByTemplateAndOrdinal(Template template, int ordinal) {
        return snippets.stream()
                .filter(snippet ->
                        Objects.equals(snippet.getTemplate(), template) &&
                                Objects.equals(snippet.getOrdinal(), ordinal)
                ).toList();
    }

    @Override
    public Snippet save(Snippet entity) {
        var saved = new Snippet(
                getOrGenerateId(entity),
                entity.getTemplate(),
                entity.getFilename(),
                entity.getContent(),
                entity.getOrdinal()
        );
        snippets.removeIf(snippet -> Objects.equals(snippet.getId(), entity.getId()));
        snippets.add(saved);
        return saved;
    }

    @Override
    public <S extends Snippet> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
        return (List<S>) snippets;
    }

    @Override
    public void deleteById(Long id) {
        snippets.removeIf(snippet -> Objects.equals(snippet.getId(), id));
    }

    @Override
    public void deleteByTemplateId(Long id) {
        snippets.removeIf(snippet -> Objects.equals(snippet.getTemplate().getId(), id));
    }

    private long getOrGenerateId(Snippet entity) {
        if (existsById(entity.getId())) {
            return entity.getId();
        }
        return idCounter.getAndIncrement();
    }

    public boolean existsById(Long id) {
        return snippets.stream().anyMatch(snippet -> Objects.equals(snippet.getId(), id));
    }
}
