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
    public void update(Long id, UpdateCategoryRequest updateCategoryRequest) {
        validateDuplicatedCategory(updateCategoryRequest.name(), null);
        Category category = categoryRepository.fetchById(id);
        category.updateName(updateCategoryRequest.name());
    }

    private void validateDuplicatedCategory(String categoryName, Member member) {
        if (categoryRepository.existsByNameAndMember(categoryName, member)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "이름이 " + categoryName + "인 카테고리가 이미 존재합니다.");
        }
    }

    public void deleteById(Long id) {
        if (templateRepository.existsByCategoryId(id)) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }
        if (id == DEFAULT_CATEGORY) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "기본 카테고리는 삭제할 수 없습니다.");
        }
        categoryRepository.deleteById(id);
    }
}
