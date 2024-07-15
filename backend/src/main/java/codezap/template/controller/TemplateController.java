package codezap.member.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codezap.template.controller.SpringDocTemplateController;
import codezap.template.dto.request.CreateTemplateResponse;
import codezap.template.dto.response.CreateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;

@RestController
@RequestMapping("/templates")
public class TemplateController implements SpringDocTemplateController {

    @PostMapping("")
    public ResponseEntity<CreateTemplateResponse> create(@RequestBody CreateTemplateRequest createTemplateRequest) {
        throw new NotImplementedException();
    }

    @GetMapping("")
    public ResponseEntity<FindAllTemplatesResponse> getTemplates() {
        throw new NotImplementedException();
    }
}
