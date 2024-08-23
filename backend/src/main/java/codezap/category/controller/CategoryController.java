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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import codezap.auth.configuration.AuthenticationPrinciple;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.service.CategoryService;
import codezap.global.validation.ValidationSequence;
import codezap.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController implements SpringDocCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CreateCategoryResponse> createCategory(
            @AuthenticationPrinciple MemberDto memberDto,
            @Validated(ValidationSequence.class) @RequestBody CreateCategoryRequest createCategoryRequest
    ) {
        CreateCategoryResponse response = categoryService.create(memberDto, createCategoryRequest);
        return ResponseEntity.created(URI.create("/categories/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<FindAllCategoriesResponse> getCategories(
            @AuthenticationPrinciple MemberDto memberDto,
            @RequestParam Long memberId
    ) {
        return ResponseEntity.ok(categoryService.findAllByMember(memberId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
            @AuthenticationPrinciple MemberDto memberDto,
            @PathVariable Long id,
            @Validated(ValidationSequence.class) @RequestBody UpdateCategoryRequest updateCategoryRequest
    ) {
        categoryService.update(memberDto, id, updateCategoryRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@AuthenticationPrinciple MemberDto memberDto, @PathVariable Long id) {
        categoryService.deleteById(memberDto, id);
        return ResponseEntity.noContent()
                .build();
    }
}
