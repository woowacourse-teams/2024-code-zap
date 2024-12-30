package codezap.category.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import codezap.auth.configuration.AuthenticationPrinciple;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateAllCategoriesRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.service.CategoryService;
import codezap.global.validation.ValidationSequence;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController implements SpringDocCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CreateCategoryResponse> createCategory(
            @AuthenticationPrinciple Member member,
            @Validated(ValidationSequence.class) @RequestBody CreateCategoryRequest request
    ) {
        CreateCategoryResponse createdCategory = categoryService.create(member, request);
        return ResponseEntity.created(URI.create("/categories/" + createdCategory.id())).body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<FindAllCategoriesResponse> getCategories(@RequestParam Long memberId) {
        return ResponseEntity.ok(categoryService.findAllByMemberId(memberId));
    }

    @PutMapping
    public ResponseEntity<Void> updateCategory(
            @AuthenticationPrinciple Member member,
            @Validated(ValidationSequence.class) @RequestBody UpdateAllCategoriesRequest request
    ) {
        categoryService.updateCategories(member, request);
        return ResponseEntity.ok().build();
    }
}
