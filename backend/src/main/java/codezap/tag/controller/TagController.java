package codezap.tag.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import codezap.member.configuration.BasicAuthentication;
import codezap.member.dto.MemberDto;
import codezap.template.dto.response.FindAllTagsResponse;
import codezap.template.service.TemplateService;

@RestController
@RequestMapping("/tags")
public class TagController implements SpringDocTagController {

    private final TemplateService templateService;

    public TagController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<FindAllTagsResponse> getTags(
            @BasicAuthentication MemberDto memberDto,
            @RequestParam Long memberId
    ) {
        FindAllTagsResponse response = templateService.findAllTagsByMemberId(memberId);
        return ResponseEntity.ok(response);
    }
}
