package codezap.extension.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.extension.domain.Extension;

public interface ExtensionRepository extends JpaRepository<Extension, Long> {
}
