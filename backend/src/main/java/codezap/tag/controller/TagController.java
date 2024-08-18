package codezap.tag.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import codezap.auth.configuration.AuthenticationPrinciple;
import codezap.member.dto.MemberDto;
import codezap.template.dto.response.FindAllTagsResponse;
import codezap.template.service.facade.MemberTemplateApplicationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController implements SpringDocTagController {

    private final MemberTemplateApplicationService memberTemplateApplicationService;

    @GetMapping
    public ResponseEntity<FindAllTagsResponse> getTags(
            @AuthenticationPrinciple MemberDto memberDto,
            @RequestParam Long memberId
    ) {
        FindAllTagsResponse response = memberTemplateApplicationService.findAllTagsByMemberId(memberDto, memberId);
        return ResponseEntity.ok(response);
    }
}
