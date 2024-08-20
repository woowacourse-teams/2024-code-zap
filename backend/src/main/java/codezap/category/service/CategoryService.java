package codezap.category.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.MemberRepository;
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(MemberDto memberDto, CreateCategoryRequest createCategoryRequest) {
        String categoryName = createCategoryRequest.name();
        Member member = memberRepository.fetchById(memberDto.id());
        validateDuplicatedCategory(categoryName, member);
        Category category = new Category(categoryName, member);
        return categoryRepository.save(category).getId();
    }

    public FindAllCategoriesResponse findAllByMember(Long memberId) {
        Member member = memberRepository.fetchById(memberId);
        return FindAllCategoriesResponse.from(categoryRepository.findAllByMemberOrderById(member));
    }

    public FindAllCategoriesResponse findAll() {
        return FindAllCategoriesResponse.from(categoryRepository.findAll());
    }

    public Category fetchById(Long id) {
        return categoryRepository.fetchById(id);
    }

    public void validateExistsById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 카테고리가 존재하지 않습니다.");
        }
    }

    @Transactional
    public void update(MemberDto memberDto, Long id, UpdateCategoryRequest updateCategoryRequest) {
        Member member = memberRepository.fetchById(memberDto.id());
        validateDuplicatedCategory(updateCategoryRequest.name(), member);
        Category category = categoryRepository.fetchById(id);
        category.validateAuthorization(member);
        category.updateName(updateCategoryRequest.name());
    }

    private void validateDuplicatedCategory(String categoryName, Member member) {
        if (categoryRepository.existsByNameAndMember(categoryName, member)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "이름이 " + categoryName + "인 카테고리가 이미 존재합니다.");
        }
    }

    public void deleteById(MemberDto memberDto, Long id) {
        Category category = categoryRepository.fetchById(id);
        Member member = memberRepository.fetchById(memberDto.id());
        category.validateAuthorization(member);

        if (templateRepository.existsByCategoryId(id)) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }
        if (category.getIsDefault()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "기본 카테고리는 삭제할 수 없습니다.");
        }
        categoryRepository.deleteById(id);
    }
}
