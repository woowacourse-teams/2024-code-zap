package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import codezap.template.domain.Tag;

public interface TagRepository {

    Tag fetchById(Long id);

    boolean existsByName(String name);

    Tag fetchByName(String name);

    Optional<Tag> findByName(String name);

    List<Tag> findByIdIn(List<Long> tagIds);

    Tag save(Tag tag);

    <S extends Tag> List<S> saveAll(Iterable<S> entities);

}
