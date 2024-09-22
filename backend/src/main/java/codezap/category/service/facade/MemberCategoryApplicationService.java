package codezap.category.service.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.service.CategoryService;
import codezap.member.domain.Member;
import codezap.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberCategoryApplicationService {

    private final MemberService memberService;
    private final CategoryService categoryService;

    @Transactional
    public CreateCategoryResponse create(Member member, CreateCategoryRequest createCategoryRequest) {
        return categoryService.create(member, createCategoryRequest);
    }

    public FindAllCategoriesResponse findAllByMember(Long memberId) {
        Member member = memberService.getById(memberId);
        return categoryService.findAllByMember(member);
    }

    @Transactional
    public void update(Member member, Long id, UpdateCategoryRequest updateCategoryRequest) {
        categoryService.update(member, id, updateCategoryRequest);
    }
}
