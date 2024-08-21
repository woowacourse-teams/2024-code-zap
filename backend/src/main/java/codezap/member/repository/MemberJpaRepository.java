package codezap.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;

@SuppressWarnings("unused")
public interface MemberJpaRepository extends MemberRepository, JpaRepository<Member, Long> {

    default Member fetchById(Long id) {
        return findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "식별자 " + id + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    default Member fetchByname(String name) {
        return findByname(name)
                .orElseThrow(() -> new CodeZapException(ErrorCode.UNAUTHORIZED_ID, "존재하지 않는 아이디 " + name + " 입니다."));
    }

    Optional<Member> findByname(String name);

    boolean existsByname(String name);
}
