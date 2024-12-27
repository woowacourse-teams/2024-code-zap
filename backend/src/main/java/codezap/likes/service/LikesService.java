package codezap.likes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.likes.domain.Likes;
import codezap.likes.repository.LikesRepository;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikesService {

    private final TemplateRepository templateRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public void like(Member member, long templateId) {
        var template = templateRepository.fetchById(templateId);
        if (isLiked(member, template)) {
            return;
        }

        likesRepository.save(new Likes(template, member));
        template.increaseLike();
    }

    public boolean isLiked(Member member, Template template) {
        return likesRepository.existsByMemberAndTemplate(member, template);
    }

    @Transactional
    public void cancelLike(Member member, long templateId) {
        var template = templateRepository.fetchById(templateId);
        if (!isLiked(member, template)) {
            return;
        }

        likesRepository.deleteByMemberAndTemplate(member, template);
        template.cancelLike();
    }

    @Transactional
    public void deleteAllByTemplateIds(List<Long> templateIds) {
        likesRepository.deleteAllByTemplateIds(templateIds);
    }
}
