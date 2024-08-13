package codezap.template.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import codezap.member.dto.MemberDto;
import codezap.member.service.MemberService;
import codezap.template.dto.response.FindAllTemplatesResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateApplicationService {
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
}
