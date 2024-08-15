package codezap.member.controller;

import jakarta.validation.Valid;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import codezap.member.configuration.AuthenticationPrinciple;
import codezap.member.dto.MemberDto;
import codezap.member.dto.request.SignupRequest;
import codezap.member.dto.request.UpdateMemberProfileRequest;
import codezap.member.dto.response.FindMemberResponse;
import codezap.member.dto.response.UpdateMemberProfileResponse;
import codezap.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController implements SpringDocMemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest request) {
        memberService.signup(request);
    }

    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    public void checkUniqueEmail(@RequestParam String email) {
        memberService.assertUniqueEmail(email);
    }

    @GetMapping("/check-username")
    @ResponseStatus(HttpStatus.OK)
    public void checkUniqueUsername(@RequestParam String username) {
        memberService.assertUniqueUsername(username);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<FindMemberResponse> findMember(@AuthenticationPrinciple MemberDto memberDto,
            @PathVariable Long id
    ) {
        throw new NotImplementedException("접근이 불가능한 기능입니다. 서버 개발자에게 문의해주세요.");
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<UpdateMemberProfileResponse> updateMemberProfile(
            @Valid @RequestBody UpdateMemberProfileRequest updateMemberProfileRequest, @PathVariable Long id
    ) {
        throw new NotImplementedException("접근이 불가능한 기능입니다. 서버 개발자에게 문의해주세요.");
    }
}
