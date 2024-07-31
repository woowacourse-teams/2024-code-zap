package codezap.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
