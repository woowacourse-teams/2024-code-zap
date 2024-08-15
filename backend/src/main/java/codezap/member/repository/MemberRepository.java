package codezap.member.repository;

import java.util.Optional;

import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;

public interface MemberRepository {

    Member fetchById(Long id);

    boolean existsByLoginId(String loginId);

    boolean existsById(Long id);

    Optional<Member> findByLoginId(String loginId);

    default Member fetchByLoginId(String loginId) {
        return findByLoginId(loginId)
                .orElseThrow(() -> new CodeZapException(HttpStatus.UNAUTHORIZED, "존재하지 않는 아이디 " + loginId + " 입니다."));
    }

    Member save(Member member);
}
