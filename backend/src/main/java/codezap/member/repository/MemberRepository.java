package codezap.member.repository;

import java.util.Optional;

import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;

public interface MemberRepository {

    Member fetchById(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(Long id);

    Optional<Member> findByEmail(String email);

    default Member fetchByEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new CodeZapException(HttpStatus.UNAUTHORIZED, "존재하지 않는 이메일 " + email + " 입니다."));
    }

    Member save(Member member);
}
