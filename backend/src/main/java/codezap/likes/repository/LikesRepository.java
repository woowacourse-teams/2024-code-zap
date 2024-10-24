package codezap.likes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.template.domain.Template;

public interface LikesRepository {

    Likes save(Likes likes);

    boolean existsByMemberAndTemplate(Member member, Template template);

    long countByTemplate(Template template);

    void deleteByMemberAndTemplate(Member member, Template template);

    void deleteAllByTemplateIds(List<Long> templateIds);

    Page<Template> findAllByMemberId(Long memberId, Pageable pageable);
}
