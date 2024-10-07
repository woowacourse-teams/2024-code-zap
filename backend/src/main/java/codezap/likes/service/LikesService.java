package codezap.likes.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import codezap.likes.domain.Likes;
import codezap.likes.repository.LikesRepository;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final TemplateRepository templateRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public void like(Member member, long templateId) {
        Template template = templateRepository.fetchById(templateId);
        if (isLiked(member, template)) {
            return;
        }
        Likes likes = new Likes(null, template, member);
        likesRepository.save(likes);
    }

    public Boolean isLiked(Member member, Template template) {
        return likesRepository.existsByMemberAndTemplate(member, template);
    }

    @Transactional
    public void cancelLike(Member member, long templateId) {
        Template template = templateRepository.fetchById(templateId);
        likesRepository.deleteByMemberAndTemplate(member, template);
    }
}