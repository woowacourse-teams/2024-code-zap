package codezap.template.service.facade;

import org.springframework.stereotype.Service;

import codezap.category.domain.Category;
import codezap.category.service.CategoryService;
import codezap.member.domain.Member;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
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

    public void update(Member member, Long templateId, UpdateTemplateRequest updateTemplateRequest) {
        Category category = categoryService.fetchById(updateTemplateRequest.categoryId());
        category.validateAuthorization(member);
        templateApplicationService.update(member, templateId, updateTemplateRequest, category);
    }
}
