package codezap.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.template.domain.Template;

public interface LikesJpaRepository extends JpaRepository<Likes, Long> {

    void deleteByMemberAndTemplate(Member member, Template template);

    boolean existsByMemberAndTemplate(Member member, Template template);

    long countByTemplate(Template template);
}
