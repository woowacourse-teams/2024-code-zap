package codezap.tag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.tag.domain.Tag;

@SuppressWarnings("unused")
public interface TagJpaRepository extends TagRepository, JpaRepository<Tag, Long> {

    default Tag fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 태그가 존재하지 않습니다."));
    }

    default Tag fetchByName(String name) {
        return findByName(name).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "이름이 " + name + "인 태그는 존재하지 않습니다."));
    }

    Optional<Tag> findByName(String name);

    @Query("select t.name from Tag t where t.name in :names")
    List<String> findNameByNamesIn(List<String> names);

    boolean existsByName(String name);
}
