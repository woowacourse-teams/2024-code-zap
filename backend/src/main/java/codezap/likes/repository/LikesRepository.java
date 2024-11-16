package codezap.likes.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LikesRepository {

    private final LikesJpaRepository likesJpaRepository;
    private final LikesQueryDslRepository likesQueryDslRepository;

    public Likes save(Likes likes) {
        return likesJpaRepository.save(likes);
    }

    public boolean existsByMemberAndTemplate(Member member, Template template) {
        return likesJpaRepository.existsByMemberAndTemplate(member, template);
    }

    public long countByTemplate(Template template) {
        return likesJpaRepository.countByTemplate(template);
    }

    public void deleteByMemberAndTemplate(Member member, Template template) {
        likesJpaRepository.deleteByMemberAndTemplate(member, template);
    }

    public void deleteAllByTemplateIds(List<Long> templateIds) {
        likesQueryDslRepository.deleteAllByTemplateIds(templateIds);
    }
}
