package codezap.thumbnail_snippet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.thumbnail_snippet.domain.ThumbnailSnippet;

public interface ThumbnailSnippetRepository extends JpaRepository<ThumbnailSnippet, Long> {
}
