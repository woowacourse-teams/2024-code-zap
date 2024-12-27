package codezap.category.service;

import java.util.HashSet;
import java.util.List;
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
        validateIds(request);

        createCategories(member, request.createCategories());
        request.updateCategories().forEach(category -> update(member, category));
        request.deleteCategoryIds().forEach(id -> delete(member, id));

        validateCategoriesCount(member, request);
    }

    private void createCategories(Member member, List<CreateCategoryRequest> requests) {
        categoryRepository.saveAll(
                requests.stream()
                        .map(createRequest -> createCategory(member, createRequest))
                        .toList()
        );
    }

    private void update(Member member, UpdateCategoryRequest request) {
        Category category = categoryRepository.fetchById(request.id());
        category.validateAuthorization(member);
        validateDefaultCategory(category);
        category.update(request.name(), request.ordinal());
    }

    private void delete(Member member, Long categoryId) {
        Category category = categoryRepository.fetchById(categoryId);
        category.validateAuthorization(member);

        validateDefaultCategory(category);
        validateHasTemplate(categoryId);
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

    private void validateIds(UpdateAllCategoriesRequest request) {
        List<Long> allIds = Stream.concat(
                request.updateCategories().stream().map(UpdateCategoryRequest::id),
                request.deleteCategoryIds().stream()
        ).toList();

        if (allIds.size() != new HashSet<>(allIds).size()) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "요청에 중복된 id가 존재합니다.");
        }
    }

    private void validateCategoriesCount(Member member, UpdateAllCategoriesRequest request) {
        if (request.updateCategories().size() + request.createCategories().size()
                != categoryRepository.countByMember(member) - 1) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "카테고리의 개수가 일치하지 않습니다.");
        }
    }
}
