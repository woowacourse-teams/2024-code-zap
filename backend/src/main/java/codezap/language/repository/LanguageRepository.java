package codezap.language.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.language.domain.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
