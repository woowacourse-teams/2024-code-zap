package codezap.template.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import codezap.auth.configuration.AuthenticationPrinciple;
import codezap.global.validation.ValidationSequence;
import codezap.member.domain.Member;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.service.facade.TemplateApplicationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/templates")
public class TemplateController implements SpringDocTemplateController {

    private final TemplateApplicationService templateApplicationService;

    @PostMapping
    public ResponseEntity<Void> createTemplate(
            @AuthenticationPrinciple Member member,
            @Validated(ValidationSequence.class) @RequestBody CreateTemplateRequest createTemplateRequest
    ) {
        Long createdTemplateId = templateApplicationService.create(member, createTemplateRequest);
        return ResponseEntity.created(URI.create("/templates/" + createdTemplateId)).build();
    }

    @GetMapping
    public ResponseEntity<FindAllTemplatesResponse> findAllTemplates(
            @AuthenticationPrinciple(required = false) Member member,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<Long> tagIds,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        if (member == null) {
            return ResponseEntity.ok(
                    templateApplicationService.findAllBy(memberId, keyword, categoryId, tagIds, pageable)
            );
        }
        return ResponseEntity.ok(
                templateApplicationService.findAllBy(memberId, keyword, categoryId, tagIds, pageable, member)
        );
    }

    @GetMapping("/like/{memberId}")
    public ResponseEntity<FindAllTemplatesResponse> findLikedTemplateLiked(
            @AuthenticationPrinciple(required = false) Member member,
            @PathVariable Long memberId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        if (member == null) {
            return ResponseEntity.ok(templateApplicationService.findAllByLiked(memberId, pageable));
        }
        return ResponseEntity.ok(templateApplicationService.findAllByLiked(member, memberId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindTemplateResponse> findTemplateById(
            @AuthenticationPrinciple(required = false) Member member,
            @PathVariable Long id
    ) {
        if (member == null) {
            return ResponseEntity.ok(templateApplicationService.findById(id));
        }
        return ResponseEntity.ok(templateApplicationService.findById(id, member));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> updateTemplate(
            @AuthenticationPrinciple Member member,
            @PathVariable Long id,
            @Validated(ValidationSequence.class) @RequestBody UpdateTemplateRequest updateTemplateRequest
    ) {
        templateApplicationService.update(member, id, updateTemplateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{ids}")
    public ResponseEntity<Void> deleteTemplates(
            @AuthenticationPrinciple Member member,
            @PathVariable List<Long> ids
    ) {
        templateApplicationService.deleteAllByMemberAndTemplateIds(member, ids);
        return ResponseEntity.noContent().build();
    }
}
