package codezap.likes.service;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Template template = templateRepository.fetchById(templateId);
        if (isLiked(member, template)) {
            return;
        }

        likesRepository.save(new Likes(template, member));
        template.updateLike();
    }

    public boolean isLiked(Member member, Template template) {
        return likesRepository.existsByMemberAndTemplate(member, template);
    }

    public Page<Template> findAllByMemberId(Long memberId, Pageable pageable) {
        return likesRepository.findAllByMemberId(memberId, pageable);
    }

    @Transactional
    public void cancelLike(Member member, long templateId) {
        Template template = templateRepository.fetchById(templateId);
        if (isLiked(member, template)) {
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
