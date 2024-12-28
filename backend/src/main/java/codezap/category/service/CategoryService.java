package codezap.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateAllCategoriesRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.repository.CategoryRepository;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryValidationService validationService;

    @Transactional
    public CreateCategoryResponse create(Member member, CreateCategoryRequest request) {
        validationService.validateDuplicatedCategory(request.name(), member);
        validationService.validateOrdinal(member, request);
        Category category = categoryRepository.save(createCategory(member, request));
        return CreateCategoryResponse.from(category);
    }

    private Category createCategory(Member member, CreateCategoryRequest request) {
        return new Category(request.name(), member, request.ordinal());
    }

    public FindAllCategoriesResponse findAllByMemberId(Long memberId) {
        return FindAllCategoriesResponse.from(categoryRepository.findAllByMemberIdOrderById(memberId));
    }

    public Category fetchById(Long id) {
        return categoryRepository.fetchById(id);
    }

    @Transactional
    public void updateCategories(Member member, UpdateAllCategoriesRequest request) {
        validationService.validateOrdinals(request);
        validationService.validateIds(request);
        validationService.validateNames(request);
        createCategories(member, request);
        updateCategories(request.updateCategories(), member);
        deleteCategories(request.deleteCategoryIds(), member);
        validationService.validateCategoriesCount(member, request);
    }

    private void createCategories(Member member, UpdateAllCategoriesRequest request) {
        categoryRepository.saveAll(request.createCategories().stream()
                .map(createRequest -> createCategory(member, createRequest))
                .toList());
    }

    private void updateCategories(List<UpdateCategoryRequest> updates, Member member) {
        updates.forEach(update -> {
            Category category = categoryRepository.fetchById(update.id());
            validationService.validateAuthorization(category, member);
            validationService.validateDefaultCategory(category);
            category.update(update.name(), update.ordinal());
        });
    }

    private void deleteCategories(List<Long> ids, Member member) {
        ids.forEach(id -> {
            Category category = categoryRepository.fetchById(id);
            validationService.validateAuthorization(category, member);
            validationService.validateDefaultCategory(category);
            validationService.validateHasTemplate(id);
            categoryRepository.deleteById(id);
        });
    }

}
