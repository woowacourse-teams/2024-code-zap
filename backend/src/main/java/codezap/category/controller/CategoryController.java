package codezap.category.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.created(URI.create("/categories/" + categoryService.create(createCategoryRequest)))
                .build();
    }

    @GetMapping
    public ResponseEntity<FindAllCategoriesResponse> getCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }
}
