package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

public class FakeThumbnailRepository implements ThumbnailRepository {

    private final AtomicLong idCounter = new AtomicLong(1);

    private final List<Thumbnail> thumbnails;

    public FakeThumbnailRepository() {
        this.thumbnails = new ArrayList<>();
    }

    @Override
    public Thumbnail fetchById(Long id) {
        return thumbnails.stream()
                .filter(thumbnail -> Objects.equals(thumbnail.getId(), id))
                .findFirst()
                .orElseThrow(
                        () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 썸네일 템플릿이 존재하지 않습니다."));
    }

    @Override
    public Thumbnail fetchByTemplate(Template template) {
        return findByTemplate(template).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND,
                        "템플릿 식별자 " + template.getId() + "에 해당하는 썸네일이 없습니다."));
    }

    @Override
    public Optional<Thumbnail> findByTemplate(Template template) {
        return thumbnails.stream()
                .filter(thumbnail -> Objects.equals(thumbnail.getTemplate(), template))
                .findFirst();
    }

    @Override
    public List<Thumbnail> findAll() {
        return thumbnails;
    }

    @Override
    public Thumbnail save(Thumbnail entity) {
        var saved = new Thumbnail(
                getOrGenerateId(entity),
                entity.getTemplate(),
                entity.getSourceCode()
        );
        thumbnails.removeIf(thumbnail -> Objects.equals(thumbnail.getId(), entity.getId()));
        thumbnails.add(saved);
        return saved;
    }

    @Override
    public void deleteByTemplateId(Long id) {
        thumbnails.removeIf(thumbnail -> Objects.equals(thumbnail.getId(), id));
    }

    private long getOrGenerateId(Thumbnail entity) {
        if (existsById(entity.getId())) {
            return entity.getId();
        }
        return idCounter.getAndIncrement();
    }

    public boolean existsById(Long id) {
        return thumbnails.stream().anyMatch(thumbnail -> Objects.equals(thumbnail.getId(), id));
    }
}
