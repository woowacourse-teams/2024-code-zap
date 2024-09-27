package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.tag.domain.Tag;
import codezap.tag.repository.TagRepository;

public class FakeTagRepository implements TagRepository {

    private final AtomicLong idCounter = new AtomicLong(1);

    private final List<Tag> tags;

    public FakeTagRepository() {
        this.tags = new ArrayList<>();
    }

    @Override
    public Tag fetchById(Long id) {
        return tags.stream()
                .filter(tag -> Objects.equals(tag.getId(), id))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 태그가 존재하지 않습니다."));
    }

    @Override
    public List<String> findNameByNamesIn(List<String> names) {
        return names.stream()
                .filter(this::existsByName)
                .toList();
    }

    private boolean existsByName(String name) {
        return tags.stream().anyMatch(tag -> Objects.equals(tag.getName(), name));
    }

    @Override
    public Tag fetchByName(String name) {
        return findByName(name).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND, "이름이 " + name + "인 태그는 존재하지 않습니다."));
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return tags.stream().filter(tag -> Objects.equals(tag.getName(), name)).findFirst();
    }

    @Override
    public <S extends Tag> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
        return (List<S>) tags;
    }

    @Override
    public Tag save(Tag entity) {
        var saved = new Tag(
                getOrGenerateId(entity),
                entity.getName()
        );
        tags.removeIf(tag -> Objects.equals(tag.getId(), entity.getId()));
        tags.add(saved);
        return saved;
    }

    private long getOrGenerateId(Tag entity) {
        if (existsById(entity.getId())) {
            return entity.getId();
        }
        return idCounter.getAndIncrement();
    }

    private boolean existsById(Long id) {
        return tags.stream().anyMatch(tag -> Objects.equals(tag.getId(), id));
    }
}
