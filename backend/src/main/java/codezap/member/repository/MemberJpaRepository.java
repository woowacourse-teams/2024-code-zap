package codezap.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;

@SuppressWarnings("unused")
public interface MemberJpaRepository extends MemberRepository, JpaRepository<Member, Long> {

    default Member fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    default Member fetchByName(String name) {
        return findByName(name)
                .orElseThrow(() -> new CodeZapException(ErrorCode.UNAUTHORIZED_ID, "존재하지 않는 아이디 " + name + " 입니다."));
    }

    default Member fetchByTemplateId(Long templateId) {
        return findByTemplateId(templateId)
                .orElseThrow(() -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "템플릿에 대한 멤버가 존재하지 않습니다."));
    }

    @Query("SELECT t.member FROM Template t WHERE t.id = :templateId")
    Optional<Member> findByTemplateId(@Param("templateId") Long templateId);

    Optional<Member> findByName(String name);

    boolean existsByName(String name);
}
