package codezap.template.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import codezap.member.dto.MemberDto;

@Controller
public class LikeController implements SpringDocsLikeController {

    @PostMapping("like/{templateId}")
    public ResponseEntity<Void> like(MemberDto memberDto) {
        throw new NotImplementedException();
    }

    @PostMapping("dislike/{templateId}")
    public ResponseEntity<Void> dislike(MemberDto memberDto) {
        throw new NotImplementedException();
    }
}
