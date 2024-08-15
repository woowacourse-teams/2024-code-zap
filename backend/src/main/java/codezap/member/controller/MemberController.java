package codezap.member.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import codezap.auth.configuration.AuthenticationPrinciple;
import codezap.member.dto.MemberDto;
import codezap.member.dto.request.SignupRequest;
import codezap.member.dto.response.FindMemberResponse;
import codezap.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController implements SpringDocMemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.created(URI.create("/members/" + memberService.signup(request))).build();
    }

    @GetMapping("/check-login-id")
    @ResponseStatus(HttpStatus.OK)
    public void checkUniqueLoginId(@RequestParam String loginId) {
        memberService.assertUniqueLoginId(loginId);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<FindMemberResponse> findMember(
            @AuthenticationPrinciple MemberDto memberDto, @PathVariable Long id
    ) {
        return ResponseEntity.ok(memberService.findMember(memberDto, id));
    }
}
