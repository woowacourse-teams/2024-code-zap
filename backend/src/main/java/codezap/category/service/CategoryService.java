package codezap.category.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CreateCategoryResponse create(Member member, CreateCategoryRequest createCategoryRequest) {
        String categoryName = createCategoryRequest.name();
        validateDuplicatedCategory(categoryName, member);
        Category category = categoryRepository.save(new Category(categoryName, member));
        return CreateCategoryResponse.from(category);
}

    public FindAllCategoriesResponse findAllByMember(Member member) {
        return FindAllCategoriesResponse.from(categoryRepository.findAllByMemberOrderById(member));
    }

    public FindAllCategoriesResponse findAll() {
        return FindAllCategoriesResponse.from(categoryRepository.findAll());
    }

    public Category fetchById(Long id) {
        return categoryRepository.fetchById(id);
    }

    @Transactional
    public void update(Member member, Long id, UpdateCategoryRequest updateCategoryRequest) {
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
}
