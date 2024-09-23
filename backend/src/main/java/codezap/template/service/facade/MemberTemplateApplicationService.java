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
import codezap.template.dto.response.FindAllTemplateItemResponse;
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

    public FindAllTagsResponse getAllTagsByMemberId(Long memberId) {
        return templateApplicationService.getAllTagsByMemberId(memberId);
    }

    public FindAllTemplatesResponse getAllTemplatesBy(Long memberId, String keyword, Long categoryId, List<Long> tagIds,
            Pageable pageable
    ) {
        FindAllTemplatesResponse findAllTemplatesResponse = templateApplicationService.findAllBy(memberId, keyword,
                categoryId, tagIds, pageable);
        List<FindAllTemplateItemResponse> findAllTemplateItemResponsesWithMember = findAllTemplatesResponse.templates()
                .stream()
                .map(findAllTemplateItemResponse -> findAllTemplateItemResponse.updateMember(
                        memberService.getByTemplateId(findAllTemplateItemResponse.id())))
                .toList();
        return findAllTemplatesResponse.updateTemplates(findAllTemplateItemResponsesWithMember);
    }

    public FindAllTemplatesResponse getAllTemplatesByWithMember(Long memberId, String keyword, Long categoryId,
            List<Long> tagIds, Pageable pageable, MemberDto loginMemberDto
    ) {
        Member loginMember = memberService.getById(loginMemberDto.id());
        FindAllTemplatesResponse findAllTemplatesResponse = templateApplicationService.findAllByWithMember(memberId,
                keyword, categoryId, tagIds, pageable, loginMember);
        List<FindAllTemplateItemResponse> findAllTemplateItemResponsesWithMember = findAllTemplatesResponse.templates()
                .stream()
                .map(findAllTemplateItemResponse -> findAllTemplateItemResponse.updateMember(
                        memberService.getByTemplateId(findAllTemplateItemResponse.id())))
                .toList();
        return findAllTemplatesResponse.updateTemplates(findAllTemplateItemResponsesWithMember);
    }

    public FindTemplateResponse getTemplateById(Long id) {
        FindTemplateResponse findTemplateResponse = templateApplicationService.getById(id);
        return findTemplateResponse.updateMember(memberService.getByTemplateId(id));
    }

    public FindTemplateResponse getTemplateByIdWithMember(Long id, MemberDto memberDto) {
        Member member = memberService.getById(memberDto.id());
        FindTemplateResponse findTemplateResponse = templateApplicationService.getByIdWithMember(id, member);
        return findTemplateResponse.updateMember(memberService.getByTemplateId(id));
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
