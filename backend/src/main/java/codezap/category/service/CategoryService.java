package codezap.category.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.template.repository.TemplateRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;

    public CategoryService(CategoryRepository categoryRepository, TemplateRepository templateRepository) {
        this.categoryRepository = categoryRepository;
        this.templateRepository = templateRepository;
    }

    @Transactional
    public Long create(CreateCategoryRequest createCategoryRequest) {
        String categoryName = createCategoryRequest.name();
        validateDuplicatedCategory(categoryName);
        Category category = new Category(categoryName);
        return categoryRepository.save(category).getId();
    }

    public FindAllCategoriesResponse findAll() {
        return FindAllCategoriesResponse.from(categoryRepository.findAll());
    }

    @Transactional
    public void update(Long id, UpdateCategoryRequest updateCategoryRequest) {
        validateDuplicatedCategory(updateCategoryRequest.name());
        Category category = categoryRepository.fetchById(id);
        category.updateName(updateCategoryRequest.name());
    }

    public void deleteById(Long id) {
        if (templateRepository.existsByCategoryId(id)) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }
        categoryRepository.deleteById(id);
    }

    private void validateDuplicatedCategory(String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "이름이 " + categoryName + "인 카테고리가 이미 존재합니다.");
        }
    }
}
