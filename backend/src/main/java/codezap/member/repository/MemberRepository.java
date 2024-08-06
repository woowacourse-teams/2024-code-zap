package codezap.member.repository;

import java.util.Optional;

import codezap.member.domain.Member;

public interface MemberRepository {

    Member fetchById(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(Long id);

    Optional<Member> findByEmail(String email);

    Member save(Member member);
}
