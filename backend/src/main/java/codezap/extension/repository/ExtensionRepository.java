package codezap.extension.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.extension.domain.Extension;

public interface ExtensionRepository extends JpaRepository<Extension, Long> {
    Optional<Extension> findByName(String name);
}
