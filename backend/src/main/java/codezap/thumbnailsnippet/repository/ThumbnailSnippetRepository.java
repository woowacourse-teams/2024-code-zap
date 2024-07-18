package codezap.thumbnailsnippet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.thumbnailsnippet.domain.ThumbnailSnippet;

public interface ThumbnailSnippetRepository extends JpaRepository<ThumbnailSnippet, Long> {
}
