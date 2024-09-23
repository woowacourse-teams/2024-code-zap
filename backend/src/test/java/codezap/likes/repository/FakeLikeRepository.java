package codezap.likes.repository;

import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.template.domain.Template;

//기존의 Fake repo 사용 코드가 터지지 않도록 임시로 구현하는 코드
public class FakeLikeRepository implements LikesRepository {
    @Override
    public Likes save(Likes likes) {
        return null;
    }

    @Override
    public boolean existsByTemplateAndMember(Template template, Member member) {
        return false;
    }

    @Override
    public long countByTemplate(Template template) {
        return 0;
    }

    @Override
    public void deleteByTemplateAndMember(Template template, Member member) {

    }
}
