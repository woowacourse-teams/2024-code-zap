package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import codezap.member.domain.Member;
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
    private final TemplateApplicationService templateApplicationService;

    public Long createTemplate(Member member, CreateTemplateRequest createTemplateRequest) {
        return templateApplicationService.createTemplate(member, createTemplateRequest);
    }

    public FindAllTagsResponse getAllTagsByMemberId(Long memberId) {
        return templateApplicationService.getAllTagsByMemberId(memberId);
    }

    public FindAllTemplatesResponse getAllTemplatesBy(Long memberId, String keyword, Long categoryId, List<Long> tagIds, Pageable pageable) {
        FindAllTemplatesResponse findAllTemplatesResponse = templateApplicationService.findAllBy(memberId, keyword, categoryId, tagIds, pageable);
        List<FindAllTemplateItemResponse> findAllTemplateItemResponsesWithMember = findAllTemplatesResponse.templates().stream()
                .map(findAllTemplateItemResponse -> findAllTemplateItemResponse.updateMember(memberService.getByTemplateId(findAllTemplateItemResponse.id())))
                .toList();
        return findAllTemplatesResponse.updateTemplates(findAllTemplateItemResponsesWithMember);
    }

    public FindTemplateResponse getTemplateById(Long id) {
        FindTemplateResponse findTemplateResponse = templateApplicationService.getById(id);
        return findTemplateResponse.updateMember(memberService.getByTemplateId(id));
    }

    public void update(Member member, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        templateApplicationService.update(member, templateId, updateTemplateRequest);
    }

    public void deleteByIds(Member member, List<Long> ids) {
        templateApplicationService.deleteByMemberAndIds(member, ids);
    }
}
