package codezap.language.repository;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.language.domain.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByName(String name);

    default Language getByName(String name) {
        return findByName(name).orElseThrow(
                () -> new NoSuchElementException(name + " 언어가 존재하지 않습니다."));
    }
}
