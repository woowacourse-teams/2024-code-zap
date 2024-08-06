package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

    default Tag fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 태그가 존재하지 않습니다."));
    }

    boolean existsByName(String name);

    Optional<Tag> findByName(String name);

    List<Tag> findByNameIn(List<String> tagNames);
}
