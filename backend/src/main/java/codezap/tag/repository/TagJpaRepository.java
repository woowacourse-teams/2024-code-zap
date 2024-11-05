package codezap.tag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.tag.domain.Tag;

@SuppressWarnings("unused")
public interface TagJpaRepository extends JpaRepository<Tag, Long> {

    @Query(value = "SELECT * FROM tag WHERE tag.name = BINARY :name", nativeQuery = true)
    Optional<Tag> findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM tag WHERE tag.name COLLATE utf8mb4_bin IN :names", nativeQuery = true)
    List<Tag> findAllByNames(@Param("names") List<String> names);
}
