package codezap.tag.repository;

import java.util.List;
import java.util.Optional;

import codezap.tag.domain.Tag;

public interface TagRepository {

    Tag fetchById(Long id);

    Tag fetchByName(String name);

    Optional<Tag> findByName(String name);

    List<Tag> findAllByNames(List<String> names);

    Tag save(Tag tag);

    <S extends Tag> List<S> saveAll(Iterable<S> entities);
}
