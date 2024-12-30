package codezap.category.service;

import org.springframework.stereotype.Service;

import codezap.category.domain.Category;
import codezap.category.domain.Ids;
import codezap.category.domain.Names;
import codezap.category.domain.Ordinals;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateAllCategoriesRequest;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryValidationService {

    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;

    public void validateDuplicatedCategory(String categoryName, Member member) {
        if (categoryRepository.existsByNameAndMember(categoryName, member)) {
            throw new CodeZapException(ErrorCode.DUPLICATE_CATEGORY, "카테고리명이 중복되었습니다.");
        }
    }

    public void validateHasTemplate(Long id) {
        if (templateRepository.existsByCategoryId(id)) {
            throw new CodeZapException(ErrorCode.CATEGORY_HAS_TEMPLATES, "템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }
    }

    public void validateOrdinal(Member member, CreateCategoryRequest request) {
        long ordinal = request.ordinal();
        long count = categoryRepository.countByMember(member);
        if (ordinal != count) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "카테고리 순서가 잘못되었습니다.");
        }
    }

    public void validateCategoriesCount(Member member, UpdateAllCategoriesRequest request) {
        if (request.updateCategories().size() + request.createCategories().size()
                != categoryRepository.countByMember(member) - 1) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "카테고리의 개수가 일치하지 않습니다.");
        }
    }

    public void validateAuthorization(Category category, Member member) {
        if (!category.hasAuthorization(member)) {
            throw new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }
    }

    public void validateDefaultCategory(Category category) {
        if (category.isDefault()) {
            throw new CodeZapException(ErrorCode.DEFAULT_CATEGORY, "기본 카테고리는 수정 및 삭제할 수 없습니다.");
        }
    }

    public void validateOrdinals(UpdateAllCategoriesRequest request) {
        try {
            Ordinals ordinals = new Ordinals(request.extractOrdinal());
            ordinals.validateOrdinals();
        } catch (IllegalArgumentException e) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, e.getMessage());
        }
    }


    public void validateIds(UpdateAllCategoriesRequest request) {
        try {
            Ids ids = new Ids(request.extractIds());
            ids.validateIds();
        } catch (IllegalArgumentException e) {
            throw new CodeZapException(ErrorCode.DUPLICATE_ID, e.getMessage());
        }
    }

    public void validateNames(UpdateAllCategoriesRequest request) {
        try {
            Names names = new Names(request.extractNames());
            names.validateNames();
        } catch (IllegalArgumentException e) {
            throw new CodeZapException(ErrorCode.DUPLICATE_CATEGORY, e.getMessage());
        }
    }
}
