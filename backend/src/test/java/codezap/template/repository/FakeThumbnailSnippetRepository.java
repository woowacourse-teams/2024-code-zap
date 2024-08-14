package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Template;
import codezap.template.domain.ThumbnailSnippet;

public class FakeThumbnailSnippetRepository implements ThumbnailSnippetRepository {

    private final AtomicLong idCounter = new AtomicLong(1);

    private final List<ThumbnailSnippet> thumbnailSnippets;

    public FakeThumbnailSnippetRepository() {
        this.thumbnailSnippets = new ArrayList<>();
    }

    @Override
    public ThumbnailSnippet fetchById(Long id) {
        return thumbnailSnippets.stream()
                .filter(thumbnailSnippet -> Objects.equals(thumbnailSnippet.getId(), id))
                .findFirst()
                .orElseThrow(
                        () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 썸네일 템플릿이 존재하지 않습니다."));
    }

    @Override
    public ThumbnailSnippet fetchByTemplate(Template template) {
        return findByTemplate(template).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND,
                        "템플릿 식별자 " + template.getId() + "에 해당하는 썸네일 스니펫이 없습니다."));
    }

    @Override
    public Optional<ThumbnailSnippet> findByTemplate(Template template) {
        return thumbnailSnippets.stream()
                .filter(thumbnailSnippet -> Objects.equals(thumbnailSnippet.getTemplate(), template))
                .findFirst();
    }

    @Override
    public List<ThumbnailSnippet> findAll() {
        return thumbnailSnippets;
    }

    @Override
    public ThumbnailSnippet save(ThumbnailSnippet entity) {
        var saved = new ThumbnailSnippet(
                getOrGenerateId(entity),
                entity.getTemplate(),
                entity.getSnippet()
        );
        thumbnailSnippets.removeIf(thumbnailSnippet -> Objects.equals(thumbnailSnippet.getId(), entity.getId()));
        thumbnailSnippets.add(saved);
        return saved;
    }

    @Override
    public void deleteByTemplateId(Long id) {
        thumbnailSnippets.removeIf(thumbnailSnippet -> Objects.equals(thumbnailSnippet.getId(), id));
    }

    private long getOrGenerateId(ThumbnailSnippet entity) {
        if (existsById(entity.getId())) {
            return entity.getId();
        }
        return idCounter.getAndIncrement();
    }

    public boolean existsById(Long id) {
        return thumbnailSnippets.stream().anyMatch(thumbnailSnippet -> Objects.equals(thumbnailSnippet.getId(), id));
    }
}
