package codezap.likes.repository;

import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.template.domain.Template;

public class FakeLikeRepository implements LikesRepository {
    @Override
    public Likes save(Likes likes) {
        return null;
    }

    @Override
    public boolean existsByMemberAndTemplate(Member member, Template template) {
        return false;
    }

    @Override
    public long countByTemplate(Template template) {
        return 0;
    }

    @Override
    public void deleteByMemberAndTemplate(Member member, Template template) {

    }
}
