package codezap.tag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.tag.domain.Tag;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class TagRepository {

    private final TagJpaRepository tagJpaRepository;

    public Tag fetchById(Long id) {
        return tagJpaRepository.findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 태그가 존재하지 않습니다."));
    }

    public Tag fetchByName(String name) {
        return tagJpaRepository.findByName(name)
                .orElseThrow(() -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "이름이 " + name + "인 태그는 존재하지 않습니다."));
    }

    public Optional<Tag> findByName(String name) {
        return tagJpaRepository.findByName(name);
    }

    public List<Tag> findAllByNames(List<String> names) {
        return tagJpaRepository.findAllByNames(names);
    }

    public Tag save(Tag tag) {
        return tagJpaRepository.save(tag);
    }

    public List<Tag> saveAll(Iterable<Tag> entities) {
        return tagJpaRepository.saveAll(entities);
    }
}
