package codezap.category.service.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.service.CategoryService;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberCategoryApplicationService {
    private final MemberService memberService;
    private final CategoryService categoryService;

    @Transactional
    public Long create(MemberDto memberDto, CreateCategoryRequest createCategoryRequest) {
        Member member = memberService.getByMemberDto(memberDto);
        return categoryService.create(member, createCategoryRequest);
    }

    public FindAllCategoriesResponse findAllByMember(MemberDto memberDto, Long memberId) {
        memberService.validateMemberIdentity(memberDto, memberId);
        Member member = memberService.getByMemberDto(memberDto);
        return categoryService.findAllByMember(member);
    }

    @Transactional
    public void update(MemberDto memberDto, Long id, UpdateCategoryRequest updateCategoryRequest) {
        Member member = memberService.getByMemberDto(memberDto);
        categoryService.update(member, id, updateCategoryRequest);
    }
}
