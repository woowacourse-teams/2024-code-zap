package codezap.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryTemplateService {

    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;

    @Transactional
    public void deleteById(Member member, Long id) {
        Category category = categoryRepository.fetchById(id);
        validateAuthorizeMember(category, member);

        if (templateRepository.existsByCategoryId(id)) {
            throw new CodeZapException(ErrorCode.CATEGORY_HAS_TEMPLATES, "템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }
        if (category.getIsDefault()) {
            throw new CodeZapException(ErrorCode.INVALID_CATEGORY_REQUEST, "기본 카테고리는 삭제할 수 없습니다.");
        }
        categoryRepository.deleteById(id);
    }

    private void validateAuthorizeMember(Category category, Member member) {
        if (!category.getMember().equals(member)) {
            throw new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }
    }
}
