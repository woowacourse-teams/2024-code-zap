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
import codezap.member.repository.MemberJpaRepository;
import codezap.template.repository.TemplateRepository;

@Service
public class CategoryService {

    public static final int DEFAULT_CATEGORY = 1;
    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final MemberJpaRepository memberJpaRepository;

    public CategoryService(CategoryRepository categoryRepository, TemplateRepository templateRepository,
            MemberJpaRepository memberJpaRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.templateRepository = templateRepository;
        this.memberJpaRepository = memberJpaRepository;
    }

    @Transactional
    public Long create(CreateCategoryRequest createCategoryRequest, MemberDto memberDto) {
        String categoryName = createCategoryRequest.name();
        Member member = memberJpaRepository.fetchById(memberDto.id());
        validateDuplicatedCategory(categoryName, member);
        Category category = new Category(categoryName, member);
        return categoryRepository.save(category).getId();
    }

    public FindAllCategoriesResponse findAllByMember(MemberDto memberDto) {
        Member member = memberJpaRepository.fetchById(memberDto.id());
        return FindAllCategoriesResponse.from(categoryRepository.findAllByMember(member));
    }

    public FindAllCategoriesResponse findAll() {
        return FindAllCategoriesResponse.from(categoryRepository.findAll());
    }

    @Transactional
    public void update(Long id, UpdateCategoryRequest updateCategoryRequest, MemberDto memberDto) {
        Member member = memberJpaRepository.fetchById(memberDto.id());
        validateDuplicatedCategory(updateCategoryRequest.name(), member);
        Category category = categoryRepository.fetchById(id);
        validateAuthorizeMember(category, member);
        category.updateName(updateCategoryRequest.name());
    }

    private void validateDuplicatedCategory(String categoryName, Member member) {
        if (categoryRepository.existsByNameAndMember(categoryName, member)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "이름이 " + categoryName + "인 카테고리가 이미 존재합니다.");
        }
    }

    public void deleteById(Long id, MemberDto memberDto) {
        Member member = memberJpaRepository.fetchById(memberDto.id());
        Category category = categoryRepository.fetchById(id);
        validateAuthorizeMember(category, member);

        if (templateRepository.existsByCategoryId(id)) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }
        if (category.getIsDefault()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "기본 카테고리는 삭제할 수 없습니다.");
        }
        categoryRepository.deleteById(id);
    }

    private void validateAuthorizeMember(Category category, Member member) {
        if (!category.getMember().equals(member)) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }
    }
}
