package codezap.template.service.facade;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import codezap.category.domain.Category;
import codezap.category.service.CategoryService;
import codezap.member.domain.Member;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryTemplateApplicationService {
    private final CategoryService categoryService;
    private final TemplateApplicationService templateApplicationService;

    public Long createTemplate(Member member, CreateTemplateRequest createTemplateRequest) {
        Category category = categoryService.fetchById(createTemplateRequest.categoryId());
        category.validateAuthorization(member);
        return templateApplicationService.createTemplate(member, category, createTemplateRequest);
    }

    public FindAllTemplatesResponse findAllBy(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Pageable pageable
    ) {
        if (categoryId == null) {
            return templateApplicationService
                    .findByMemberKeywordAndCategoryOrTagIds(memberId, keyword, tagIds, pageable);
        }
        categoryService.validateExistsById(categoryId);
        return templateApplicationService
                .findByMemberKeywordOrTagIds(memberId, keyword, categoryId, tagIds, pageable);
    }

    public void update(Member member, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Category category = categoryService.fetchById(updateTemplateRequest.categoryId());
        category.validateAuthorization(member);
        templateApplicationService.update(member, templateId, updateTemplateRequest, category);
    }
}
