package codezap.likes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import codezap.auth.configuration.AuthenticationPrinciple;
import codezap.likes.service.LikesService;
import codezap.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LikesController implements SpringDocsLikesController {

    private final LikesService likesService;

    @PostMapping("like/{templateId}")
    public ResponseEntity<Void> like(@AuthenticationPrinciple MemberDto memberDto, @PathVariable long templateId) {
        likesService.like(memberDto, templateId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("like/{templateId}")
    public ResponseEntity<Void> cancelLike(@AuthenticationPrinciple MemberDto memberDto, @PathVariable long templateId) {
        likesService.cancelLike(memberDto, templateId);
        return ResponseEntity.ok().build();
    }
}