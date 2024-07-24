package codezap.template.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateByIdResponse;
import codezap.template.service.TemplateService;

@RestController
@RequestMapping("/templates")
public class TemplateController implements SpringDocTemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateTemplateRequest createTemplateRequest) {
        return ResponseEntity.created(URI.create("/templates/" + templateService.create(createTemplateRequest)))
                .build();
    }

    @GetMapping
    public ResponseEntity<FindAllTemplatesResponse> getTemplates() {
        return ResponseEntity.ok(templateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindTemplateByIdResponse> getTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.findById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> updateTemplate(@PathVariable Long id, UpdateTemplateRequest updateTemplateRequest) {
        throw new NotImplementedException();
    }

}
