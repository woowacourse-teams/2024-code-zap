package codezap.category.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
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
    public CreateCategoryResponse create(Member member, CreateCategoryRequest request) {
        validateDuplicatedCategory(request.name(), member);
        validateOrdinal(member, request);
        Category category = categoryRepository.save(createCategory(member, request));
        return CreateCategoryResponse.from(category);
    }

    private Category createCategory(Member member, CreateCategoryRequest request) {
        return new Category(request.name(), member, request.ordinal());
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
        validateDuplicateNameRequest(request);
        validateOrdinals(request);
        validateIds(request);

        createCategories(member, request);
        for (UpdateCategoryRequest updateCategory : request.updateCategories()) {
            update(member, updateCategory);
        }
        for (Long deleteCategoryId : request.deleteCategoryIds()) {
            delete(member, deleteCategoryId);
        }
    }

    private void createCategories(Member member, UpdateAllCategoriesRequest request) {
        categoryRepository.saveAll(
                request.createCategories().stream()
                        .map(createRequest -> createCategory(member, createRequest))
                        .toList()
        );
    }

    private void update(Member member, UpdateCategoryRequest request) {
        Category category = categoryRepository.fetchById(request.id());
        validateDefaultCategory(category);
        category.validateAuthorization(member);
        category.update(request.name(), request.ordinal());
    }

    private void delete(Member member, Long categoryId) {
        Category category = categoryRepository.fetchById(categoryId);
        category.validateAuthorization(member);

        validateHasTemplate(categoryId);
        validateDefaultCategory(category);
        categoryRepository.deleteById(categoryId);
    }

    private void validateDuplicatedCategory(String categoryName, Member member) {
        if (categoryRepository.existsByNameAndMember(categoryName, member)) {
            throw new CodeZapException(ErrorCode.DUPLICATE_CATEGORY, "이름이 " + categoryName + "인 카테고리가 이미 존재합니다.");
        }
    }

    private void validateDefaultCategory(Category category) {
        if (category.isDefault()) {
            throw new CodeZapException(ErrorCode.DEFAULT_CATEGORY, "기본 카테고리는 수정 및 삭제할 수 없습니다.");
        }
    }

    private void validateHasTemplate(Long id) {
        if (templateRepository.existsByCategoryId(id)) {
            throw new CodeZapException(ErrorCode.CATEGORY_HAS_TEMPLATES, "템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }
    }

    private void validateDuplicateNameRequest(UpdateAllCategoriesRequest request) {
        List<String> allNames = Stream.concat(
                request.createCategories().stream().map(CreateCategoryRequest::name),
                request.updateCategories().stream().map(UpdateCategoryRequest::name)
        ).toList();

        if (allNames.size() != new HashSet<>(allNames).size()) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "요청에 중복된 카테고리 이름이 존재합니다.");
        }
    }

    private void validateOrdinal(Member member, CreateCategoryRequest request) {
        long ordinal = request.ordinal();
        long count = categoryRepository.countByMember(member);
        if (ordinal != count) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "카테고리 순서가 잘못되었습니다.");
        }
    }

    private void validateOrdinals(UpdateAllCategoriesRequest request) {
        List<Long> allOrdinals = Stream.concat(
                request.createCategories().stream().map(CreateCategoryRequest::ordinal),
                request.updateCategories().stream().map(UpdateCategoryRequest::ordinal)
        ).sorted().toList();

        boolean isSequential = IntStream.range(0, allOrdinals.size())
                .allMatch(i -> allOrdinals.get(i) == i + 1);

        if (!isSequential) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "카테고리 순서가 연속적이지 않습니다.");
        }
    }

    private void validateIds(UpdateAllCategoriesRequest request) {
        List<Long> allIds = Stream.concat(
                request.updateCategories().stream().map(UpdateCategoryRequest::id),
                request.deleteCategoryIds().stream()
        ).toList();

        if (allIds.size() != new HashSet<>(allIds).size()) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "요청에 중복된 id가 존재합니다.");
        }
    }
}
