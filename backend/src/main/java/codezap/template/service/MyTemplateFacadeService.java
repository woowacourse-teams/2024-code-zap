package codezap.template.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import codezap.member.dto.MemberDto;
import codezap.member.service.MemberService;
import codezap.template.dto.response.FindAllMyTemplatesResponse;

@Service
public class MyTemplateFacadeService {
    private final MemberService memberService;
    private final TemplateService templateService;

    public MyTemplateFacadeService(MemberService memberService, TemplateService templateService
    ) {
        this.memberService = memberService;
        this.templateService = templateService;
    }

    public FindAllMyTemplatesResponse searchMyTemplatesContainTopic(
            MemberDto memberDto, Long memberId, String topic, Pageable pageable
    ) {
        memberService.validateMemberId(memberDto, memberId);
        return templateService.findContainTopic(memberId, topic, pageable);
    }


}
