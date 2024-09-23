package codezap.like.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import codezap.like.domain.Likes;
import codezap.like.repository.LikesRepository;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final TemplateRepository templateRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;

    public void like(MemberDto memberDto, long templateId) {
        Template template = templateRepository.fetchById(templateId);
        Member member = memberRepository.fetchById(memberDto.id());
        if (likesRepository.existsByTemplateAndMember(template, member)) {
            return;
        }
        Likes likes = template.like(member);
        likesRepository.save(likes);
    }

    public long getLikesCount(Template template) {
        return likesRepository.countByTemplate(template);
    }

    public Boolean isLike(Template template, Member member) {
        return likesRepository.existsByTemplateAndMember(template, member);
    }

    @Transactional
    public void cancelLike(MemberDto memberDto, long templateId) {
        Template template = templateRepository.fetchById(templateId);
        Member member = memberRepository.fetchById(memberDto.id());
        likesRepository.deleteByTemplateAndMember(template, member);
    }
}
