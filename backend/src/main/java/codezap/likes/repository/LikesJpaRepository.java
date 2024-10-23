package codezap.likes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.likes.domain.Likes;
import codezap.template.domain.Template;

public interface LikesJpaRepository extends LikesRepository, JpaRepository<Likes, Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Likes l WHERE l.template.id in :templateIds")
    void deleteAllByTemplateIds(@Param(value = "templateIds") List<Long> templateIds);

    @Query("""
            SELECT l.template
            FROM Likes l
            WHERE l.member.id = :memberId AND (l.template.member.id = :memberId OR l.template.visibility = 'PUBLIC')
            """)
    Page<Template> findAllByMemberId(@Param(value = "memberId") Long memberId, Pageable pageable);
}
