package codezap.member.repository;

import codezap.member.domain.Member;

public interface MemberRepository {

    Member fetchById(Long id);

    Member fetchByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    boolean existsById(Long id);

    Member save(Member member);
}
