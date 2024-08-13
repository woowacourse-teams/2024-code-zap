package codezap.member.controller;

import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import codezap.member.configuration.BasicAuthentication;
import codezap.member.dto.LoginRequest;
import codezap.member.dto.LoginResponse;
import codezap.member.dto.MemberDto;
import codezap.member.dto.SignupRequest;
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

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        MemberDto member = memberService.login(request);
        String basicAuth = HttpHeaders.encodeBasicAuth(member.email(), member.password(), StandardCharsets.UTF_8);
        ResponseCookie cookie = ResponseCookie.from(HttpHeaders.AUTHORIZATION, basicAuth)
                .maxAge(-1)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new LoginResponse(member.id(), member.username());
    }

    @GetMapping("/login/check")
    @ResponseStatus(HttpStatus.OK)
    public void checkLogin(HttpServletRequest request) {
        memberService.checkLogin(request.getCookies());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(HttpHeaders.AUTHORIZATION, "")
                .maxAge(0)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<FindMemberResponse> findMember(@BasicAuthentication MemberDto memberDto, @PathVariable Long id) {
        throw new NotImplementedException("접근이 불가능한 기능입니다. 서버 개발자에게 문의해주세요.");
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<UpdateMemberProfileResponse> updateMemberProfile(
            @Valid @RequestBody UpdateMemberProfileRequest updateMemberProfileRequest, @PathVariable Long id
    ) {
        throw new NotImplementedException("접근이 불가능한 기능입니다. 서버 개발자에게 문의해주세요.");
    }
}
