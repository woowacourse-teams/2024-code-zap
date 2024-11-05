package codezap.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;

@SuppressWarnings("unused")
public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    boolean existsByName(String name);
}
