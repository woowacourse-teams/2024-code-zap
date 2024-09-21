package codezap.like.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import codezap.member.dto.MemberDto;

@Controller
public class LikeController implements SpringDocsLikeController {

    @PostMapping("like/{templateId}")
    public ResponseEntity<Void> like(MemberDto memberDto) {
        throw new NotImplementedException();
    }

    @DeleteMapping("like/{templateId}")
    public ResponseEntity<Void> cancleLike(MemberDto memberDto) {
        throw new NotImplementedException();
    }
}
