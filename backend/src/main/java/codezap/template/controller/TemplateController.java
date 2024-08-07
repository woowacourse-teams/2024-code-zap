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

import codezap.global.validation.ValidationSequence;
import codezap.member.configuration.BasicAuthentication;
import codezap.member.dto.MemberDto;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.ExploreTemplatesResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.service.TemplateService;

@RestController
@RequestMapping("/templates")
public class TemplateController implements SpringDocTemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @Validated(ValidationSequence.class) @RequestBody CreateTemplateRequest createTemplateRequest,
            @BasicAuthentication MemberDto memberDto
    ) {
        Long createdTemplateId = templateService.createTemplate(createTemplateRequest, memberDto);
        return ResponseEntity.created(URI.create("/templates/" + createdTemplateId))
                .build();
    }

    @GetMapping
    public ResponseEntity<FindAllTemplatesResponse> getTemplates(
            @BasicAuthentication MemberDto memberDto,
            @RequestParam Long memberId,
            @RequestParam("keyword") String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<String> tags,
            @PageableDefault(size = 20, page = 1) Pageable pageable
    ) {
        FindAllTemplatesResponse response =
                templateService.findAllBy(memberId, keyword, categoryId, tags, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/explore")
    public ResponseEntity<ExploreTemplatesResponse> explore() {
        return ResponseEntity.ok(templateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindTemplateResponse> getTemplateById(@PathVariable Long id,
            @BasicAuthentication MemberDto memberDto
    ) {
        return ResponseEntity.ok(templateService.findByIdAndMember(id, memberDto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> updateTemplate(
            @PathVariable Long id,
            @Validated(ValidationSequence.class) @RequestBody UpdateTemplateRequest updateTemplateRequest,
            @BasicAuthentication MemberDto memberDto
    ) {
        templateService.update(id, updateTemplateRequest, memberDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id, @BasicAuthentication MemberDto memberDto) {
        templateService.deleteById(id, memberDto);
        return ResponseEntity.noContent().build();
    }
}
