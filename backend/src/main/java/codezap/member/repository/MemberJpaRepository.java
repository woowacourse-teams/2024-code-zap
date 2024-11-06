package codezap.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.member.domain.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    boolean existsByName(String name);
}
