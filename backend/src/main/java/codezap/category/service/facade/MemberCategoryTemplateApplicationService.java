package codezap.category.service.facade;

import org.springframework.stereotype.Service;

import codezap.category.service.CategoryTemplateService;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberCategoryTemplateApplicationService {
    private final MemberService memberService;
    private final CategoryTemplateService categoryTemplateService;

    public void deleteById(MemberDto memberDto, Long id) {
        Member member = memberService.getById(memberDto.id());
        categoryTemplateService.deleteById(member, id);
    }

}
