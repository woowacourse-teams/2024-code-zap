package codezap.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.ThumbnailSnippet;

public interface ThumbnailSnippetRepository extends JpaRepository<ThumbnailSnippet, Long> {
}
