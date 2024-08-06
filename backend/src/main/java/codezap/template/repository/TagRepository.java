package codezap.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String name);

    Optional<Tag> findByName(String name);
}
