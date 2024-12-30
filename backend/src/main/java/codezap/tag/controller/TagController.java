package codezap.tag.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import codezap.tag.dto.response.FindAllTagsResponse;
import codezap.tag.service.TagService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController implements SpringDocTagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<FindAllTagsResponse> getTags(@RequestParam Long memberId) {
        FindAllTagsResponse response = tagService.findAllByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top")
    public ResponseEntity<FindAllTagsResponse> getTopTags(@RequestParam(defaultValue = "10") int size) {
        FindAllTagsResponse response = tagService.getTopTags(size);
        return ResponseEntity.ok(response);
    }
}
