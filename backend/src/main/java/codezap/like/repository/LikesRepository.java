package codezap.like.repository;

import codezap.like.domain.Likes;
import codezap.member.domain.Member;
import codezap.template.domain.Template;

public interface LikesRepository {
    Likes save(Likes likes);

    boolean existsByTemplateAndMember(Template template, Member member);

    long countByTemplate(Template template);

    void deleteByTemplateAndMember(Template template, Member member);
}
