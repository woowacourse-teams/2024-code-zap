package codezap.snippet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.snippet.domain.Snippet;

public interface SnippetRepository extends JpaRepository<Snippet, Long> {
}
