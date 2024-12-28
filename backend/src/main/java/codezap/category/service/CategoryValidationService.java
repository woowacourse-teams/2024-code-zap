package codezap.category.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

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
            throw new CodeZapException(ErrorCode.DUPLICATE_CATEGORY, "이름이 " + categoryName + "인 카테고리가 이미 존재합니다.");
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

    public void validateOrdinals(UpdateAllCategoriesRequest request) {
        List<Integer> allOrdinals = request.extractOrdinal();
        if (!isSequential(allOrdinals)) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "순서가 잘못되었습니다.");
        }
    }

    public void validateIds(UpdateAllCategoriesRequest request) {
        List<Long> allIds = request.extractIds();
        if (hasDuplicates(allIds)) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "id가 중복되었습니다.");
        }
    }

    public void validateNames(UpdateAllCategoriesRequest request) {
        List<String> allNames = request.extractNames();
        if (hasDuplicates(allNames)) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "카테고리명이 중복되었습니다.");
        }
    }

    private boolean hasDuplicates(List<?> items) {
        return items.size() != new HashSet<>(items).size();
    }

    private boolean isSequential(List<Integer> ordinals) {
        return IntStream.range(0, ordinals.size())
                .allMatch(index -> ordinals.get(index) == index + 1);
    }
}
