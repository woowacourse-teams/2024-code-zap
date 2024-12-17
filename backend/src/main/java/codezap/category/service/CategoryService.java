package codezap.category.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.DeleteAllCategoriesRequest;
import codezap.category.dto.request.DeleteCategoryRequest;
import codezap.category.dto.request.UpdateAllCategoriesRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;

    @Transactional
    public CreateCategoryResponse create(Member member, CreateCategoryRequest createCategoryRequest) {
        String categoryName = createCategoryRequest.name();
        validateDuplicatedCategory(categoryName, member);
        long count = categoryRepository.countByMember(member);
        Category category = categoryRepository.save(new Category(categoryName, member, count + 1));
        return CreateCategoryResponse.from(category);
    }

    public FindAllCategoriesResponse findAllByMemberId(Long memberId) {
        return FindAllCategoriesResponse.from(categoryRepository.findAllByMemberIdOrderById(memberId));
    }

    public FindAllCategoriesResponse findAll() {
        return FindAllCategoriesResponse.from(categoryRepository.findAll());
    }

    public Category fetchById(Long id) {
        return categoryRepository.fetchById(id);
    }

    @Transactional
    public void updateCategories(Member member, UpdateAllCategoriesRequest request) {
        for (UpdateCategoryRequest categoryRequest : request.categories()) {
            update(member, categoryRequest);
        }
    }

    private void update(Member member, UpdateCategoryRequest request) {
        validateDuplicatedCategory(request.name(), member);
        Category category = categoryRepository.fetchById(request.id());
        category.validateAuthorization(member);
        category.update(request.name(), request.ordinal());
    }

    private void validateDuplicatedCategory(String categoryName, Member member) {
        if (categoryRepository.existsByNameAndMember(categoryName, member)) {
            throw new CodeZapException(ErrorCode.DUPLICATE_CATEGORY, "이름이 " + categoryName + "인 카테고리가 이미 존재합니다.");
        }
    }

    @Transactional
    public void deleteCategories(Member member, DeleteAllCategoriesRequest request) {
        List<DeleteCategoryRequest> sortedCategoryRequests = request.categories().stream()
                .sorted(Comparator.comparing(DeleteCategoryRequest::ordinal).reversed())
                .toList();
        for (DeleteCategoryRequest categoryRequest : sortedCategoryRequests) {
            delete(member, categoryRequest);
        }
    }

    private void delete(Member member, DeleteCategoryRequest request) {
        Long id = request.id();
        Category category = categoryRepository.fetchById(id);
        category.validateAuthorization(member);

        if (templateRepository.existsByCategoryId(id)) {
            throw new CodeZapException(ErrorCode.CATEGORY_HAS_TEMPLATES, "템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }
        if (category.isDefault()) {
            throw new CodeZapException(ErrorCode.DEFAULT_CATEGORY, "기본 카테고리는 삭제할 수 없습니다.");
        }
        categoryRepository.deleteById(id);
        categoryRepository.shiftOrdinal(member, request.ordinal());
    }
}
