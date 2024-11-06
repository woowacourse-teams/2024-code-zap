package codezap.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.Thumbnail;

public interface ThumbnailJpaRepository extends JpaRepository<Thumbnail, Long> {

    void deleteByTemplateId(Long id);
}
