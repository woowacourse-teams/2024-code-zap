package codezap.category.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.service.CategoryService;
import codezap.global.validation.ValidationSequence;

@RestController
@RequestMapping("/categories")
public class CategoryController implements SpringDocCategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(
            @Validated(ValidationSequence.class) @RequestBody CreateCategoryRequest createCategoryRequest
    ) {
        Long createdCategoryId = categoryService.create(createCategoryRequest);
        return ResponseEntity.created(URI.create("/categories/" + createdCategoryId))
                .build();
    }

    @GetMapping
    public ResponseEntity<FindAllCategoriesResponse> getCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable Long id,
            @Validated(ValidationSequence.class) @RequestBody UpdateCategoryRequest updateCategoryRequest
    ) {
        categoryService.update(id, updateCategoryRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
