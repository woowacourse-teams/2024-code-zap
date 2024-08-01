package codezap.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import codezap.member.dto.SignupRequest;
import codezap.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    void signup(@RequestBody SignupRequest request) {
        memberService.signup(request);
    }

    @GetMapping("/check-email")
    ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String value) {
        boolean isDuplicate = memberService.isEmailDuplicate(value);
        return ResponseEntity.ok(isDuplicate);
    }

    @GetMapping("/check-username")
    ResponseEntity<Boolean> checkUsernameDuplicate(@RequestParam String value) {
        boolean isDuplicate = memberService.isUsernameDuplicate(value);
        return ResponseEntity.ok(isDuplicate);
    }
}
