package codezap.likes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.template.domain.Template;

public interface LikesJpaRepository extends JpaRepository<Likes, Long> {

    void deleteByMemberAndTemplate(Member member, Template template);

    boolean existsByMemberAndTemplate(Member member, Template template);

    long countByTemplate(Template template);

    @Query("""
            SELECT l.template
            FROM Likes l
            WHERE l.member.id = :memberId AND (l.template.member.id = :memberId OR l.template.visibility = 'PUBLIC')
            """)
    Page<Template> findAllByMemberId(@Param(value = "memberId") Long memberId, Pageable pageable);
}
