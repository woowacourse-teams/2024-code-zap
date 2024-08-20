package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.service.MemberService;
import codezap.tag.dto.response.FindAllTagsResponse;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberTemplateApplicationService {
    private final MemberService memberService;
    private final CategoryTemplateApplicationService categoryTemplateApplicationService;
    private final TemplateApplicationService templateApplicationService;

    public Long createTemplate(MemberDto memberDto, CreateTemplateRequest createTemplateRequest) {
        Member member = memberService.getById(memberDto.id());
        return categoryTemplateApplicationService.createTemplate(member, createTemplateRequest);
    }

    public FindAllTagsResponse getAllTagsByMemberId(MemberDto memberDto, Long memberId) {
        memberService.validateMemberIdentity(memberDto, memberId);
        return templateApplicationService.getAllTagsByMemberId(memberId);
    }

    public FindTemplateResponse getByIdAndMember(MemberDto memberDto, Long id) {
        Member member = memberService.getById(memberDto.id());
        return templateApplicationService.getByMemberAndId(member, id);
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
        return categoryTemplateApplicationService.findAllBy(memberId, keyword, categoryId, tagIds, pageable);
    }

    public void update(MemberDto memberDto, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Member member = memberService.getById(memberDto.id());
        categoryTemplateApplicationService.update(member, templateId, updateTemplateRequest);
    }

    public void deleteByIds(MemberDto memberDto, List<Long> ids) {
        Member member = memberService.getById(memberDto.id());
        templateApplicationService.deleteByMemberAndIds(member, ids);
    }
}
