package codezap.likes.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import codezap.likes.domain.Likes;
import codezap.likes.repository.LikesRepository;
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
        if (isLike(template, member)) {
            return;
        }
        Likes likes = new Likes(null, template, member);
        likesRepository.save(likes);
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
