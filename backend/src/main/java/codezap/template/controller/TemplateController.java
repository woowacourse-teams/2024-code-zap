package codezap.template.controller;

import java.net.URI;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
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
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllMyTemplatesResponse;
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
            @Validated(ValidationSequence.class) @RequestBody CreateTemplateRequest createTemplateRequest
    ) {
        Long createdTemplateId = templateService.createTemplate(createTemplateRequest);
        return ResponseEntity.created(URI.create("/templates/" + createdTemplateId))
                .build();
    }

    @GetMapping
    public ResponseEntity<FindAllTemplatesResponse> getTemplates(
            //@RequestParam Long memberId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false, defaultValue = "1") Long categoryId,
            @RequestParam(required = false) List<String> tagNames
    ) {
        return ResponseEntity.ok(
                templateService.findAllBy(PageRequest.of(pageNumber - 1, pageSize), categoryId, tagNames));
    }

    @GetMapping("/explore")
    public ResponseEntity<ExploreTemplatesResponse> explore() {
        return ResponseEntity.ok(templateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindTemplateResponse> getTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<FindAllMyTemplatesResponse> getMyTemplatesContainTopic(@RequestParam("topic") String topic) {
        return ResponseEntity.ok(templateService.findContainTopic(topic));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> updateTemplate(
            @PathVariable Long id,
            @Validated(ValidationSequence.class) @RequestBody UpdateTemplateRequest updateTemplateRequest
    ) {
        templateService.update(id, updateTemplateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
