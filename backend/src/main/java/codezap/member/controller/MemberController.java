package codezap.member.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import codezap.member.dto.SignupRequest;
import codezap.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController implements SpringDocMemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest request) {
        long memberId = memberService.signup(request);
        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkUniqueEmail(@RequestParam String email) {
        boolean isUnique = memberService.isUniqueEmail(email);
        return ResponseEntity.ok(isUnique);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUniqueUsername(@RequestParam String username) {
        boolean isUnique = memberService.isUniqueUsername(username);
        return ResponseEntity.ok(isUnique);
    }
}
