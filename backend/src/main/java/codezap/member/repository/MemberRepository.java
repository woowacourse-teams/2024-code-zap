package codezap.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
