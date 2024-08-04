package codezap.member.controller;

import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import codezap.member.dto.LoginRequest;
import codezap.member.dto.MemberDto;
import codezap.member.dto.SignupRequest;
import codezap.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController implements SpringDocMemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody SignupRequest request) {
        memberService.signup(request);
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

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        MemberDto member = memberService.login(request);
        String basicAuth = HttpHeaders.encodeBasicAuth(member.email(), member.password(), StandardCharsets.UTF_8);
        ResponseCookie cookie = ResponseCookie.from(HttpHeaders.AUTHORIZATION, basicAuth)
                .maxAge(-1)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @GetMapping("/login/check")
    @ResponseStatus(HttpStatus.OK)
    public void checkLogin(HttpServletRequest request) {
        memberService.login(request.getCookies());
    }
}
