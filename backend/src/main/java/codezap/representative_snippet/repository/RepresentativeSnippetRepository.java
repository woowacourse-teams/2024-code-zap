package codezap.representative_snippet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.representative_snippet.domain.RepresentativeSnippet;

public interface RepresentativeSnippetRepository extends JpaRepository<RepresentativeSnippet, Long> {
}
