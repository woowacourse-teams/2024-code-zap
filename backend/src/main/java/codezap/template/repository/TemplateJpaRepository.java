package codezap.template.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Template;

@SuppressWarnings("unused")
public interface TemplateJpaRepository extends TemplateRepository, JpaRepository<Template, Long>,
        JpaSpecificationExecutor<Template> {

    default Template fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 템플릿이 존재하지 않습니다."));
    }

    List<Template> findByMemberId(Long id);

    @Query("""
            SELECT t.id
            FROM Template t
            WHERE t.member.id = :memberId
            """)
    List<Long> findAllIdsByMemberId(Long memberId);

    Page<Template> findAll(Specification<Template> specification, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);
}
