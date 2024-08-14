package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import codezap.template.domain.Tag;

public interface TagRepository {

    Tag fetchById(Long id);

    Tag fetchByName(String name);

    Optional<Tag> findByName(String name);

    boolean existsByName(String name);

    Tag save(Tag tag);

    <S extends Tag> List<S> saveAll(Iterable<S> entities);

}
