package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.service.MemberService;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTagsResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberTemplateApplicationService {
    private final MemberService memberService;
    private final TemplateApplicationService templateApplicationService;
    private final TagTemplateApplicationService tagTemplateApplicationService;

    public Long createTemplate(MemberDto memberDto, CreateTemplateRequest createTemplateRequest) {
        Member member = memberService.fetchByMemberDto(memberDto);
        return templateApplicationService.createTemplate(member, createTemplateRequest);
    }

    public FindAllTemplatesResponse findAllBy(
            MemberDto memberDto,
            long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        memberService.validateMemberIdentity(memberDto, memberId);
        return templateApplicationService.findAllBy(memberId, keyword, categoryId, tagIds, pageable);
    }

    public FindAllTagsResponse findAllTagsByMemberId(MemberDto memberDto, Long memberId) {
        memberService.validateMemberIdentity(memberDto, memberId);
        return tagTemplateApplicationService.findAllTagsByMemberId(memberId);
    }

    public FindTemplateResponse findByIdAndMember(MemberDto memberDto, Long id) {
        Member member = memberService.fetchByMemberDto(memberDto);
        return tagTemplateApplicationService.findByIdAndMember(member, id);
    }

    public void update(MemberDto memberDto, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Member member = memberService.fetchByMemberDto(memberDto);
        templateApplicationService.update(member, templateId, updateTemplateRequest);
    }

    public void deleteByIds(MemberDto memberDto, List<Long> ids) {
        Member member = memberService.fetchByMemberDto(memberDto);
        tagTemplateApplicationService.deleteByIds(member, ids);
    }
}
