package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.service.CategoryService;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateApplicationService {
    private final CategoryService categoryService;
    private final TagTemplateApplicationService tagTemplateApplicationService;

    @Transactional
    public Long createTemplate(Member member, CreateTemplateRequest createTemplateRequest) {
        Category category = categoryService.fetchById(createTemplateRequest.categoryId());
        validateCategoryAuthorizeMember(member, category);
        return tagTemplateApplicationService.createTemplate(member, category, createTemplateRequest);
    }

    private void validateCategoryAuthorizeMember(Member member, Category category) {
        if (!category.getMember().equals(member)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "해당 카테고리에 대한 권한이 없습니다.");
        }
    }

    public FindAllTemplatesResponse findAllBy(
            long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

        if (categoryId == null) {
            return tagTemplateApplicationService.findAllBy(memberId, keyword, tagIds, pageable);
        }
        categoryService.validateExistsById(categoryId);
        return tagTemplateApplicationService.findAllBy(memberId, keyword, categoryId, tagIds, pageable);
    }

    @Transactional
    public void update(Member member, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Category category = categoryService.fetchById(updateTemplateRequest.categoryId());
        validateCategoryAuthorizeMember(member, category);
        tagTemplateApplicationService.update(member, category, templateId, updateTemplateRequest);
    }
}
