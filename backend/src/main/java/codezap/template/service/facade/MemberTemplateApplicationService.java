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
import codezap.template.service.TemplateService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberTemplateApplicationService {
    private final MemberService memberService;
    private final TemplateService templateService;

    public FindAllTemplatesResponse findAllBy(
            MemberDto memberDto,
            long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        memberService.validateMemberIdentity(memberDto, memberId);
        return templateService.findAllBy(memberId, keyword, categoryId, tagIds, pageable);
    }

    public Long createTemplate(MemberDto memberDto, CreateTemplateRequest createTemplateRequest) {
        Member member = memberService.fetchByMemberDto(memberDto);
        return templateService.createTemplate(member, createTemplateRequest);
    }

    public FindAllTagsResponse findAllTagsByMemberId(MemberDto memberDto, Long memberId) {
        memberService.validateMemberIdentity(memberDto, memberId);
        return templateService.findAllTagsByMemberId(memberId);
    }

    public FindTemplateResponse findByIdAndMember(MemberDto memberDto, Long id) {
        Member member = memberService.fetchByMemberDto(memberDto);
        return templateService.findByIdAndMember(member, id);
    }

    public void update(MemberDto memberDto, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Member member = memberService.fetchByMemberDto(memberDto);
        templateService.update(member, templateId, updateTemplateRequest);
    }

    public void deleteByIds(MemberDto memberDto, List<Long> ids) {
        Member member = memberService.fetchByMemberDto(memberDto);
        templateService.deleteByIds(member, ids);
    }
}
