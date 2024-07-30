package codezap.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Long create(CreateCategoryRequest createCategoryRequest) {
        Category category = new Category(createCategoryRequest.name());
        return categoryRepository.save(category).getId();
    }
}
