package codezap.member.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

import jakarta.servlet.http.Cookie;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.member.dto.LoginRequest;
import codezap.member.dto.MemberDto;
import codezap.member.dto.SignupRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signup(SignupRequest request) {
        assertUniqueEmail(request.email());
        assertUniqueUsername(request.username());
        Member member = new Member(request.email(), request.password(), request.username());
        return memberRepository.save(member);
    }

    public MemberDto login(LoginRequest request) {
        return login(request.email(), request.password());
    }

    public MemberDto login(Cookie[] cookies) {
        String authHeaderValue = getAuthCookieValue(cookies);
        String[] credentials = decodeCredentials(authHeaderValue);
        String email = credentials[0];
        String password = credentials[1];
        return login(email, password);
    }

    private MemberDto login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(this::throwUnauthorized);
        if (!member.matchPassword(password)) {
            throwUnauthorized();
        }
        return MemberDto.from(member);
    }

    private String getAuthCookieValue(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> Objects.equals(cookie.getName(), HttpHeaders.AUTHORIZATION))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(this::throwUnauthorized);
    }

    private String[] decodeCredentials(String encodedCredentials) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials.getBytes(StandardCharsets.UTF_8));
        String decodedString = new String(decodedBytes);
        return decodedString.split(":");
    }

    private void assertUniqueEmail(String email) {
        if (!isUniqueEmail(email)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "이메일이 이미 존재합니다.");
        }
    }

    private void assertUniqueUsername(String username) {
        if (!isUniqueUsername(username)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "사용자명이 이미 존재합니다.");
        }
    }

    public boolean isUniqueEmail(String email) {
        return !memberRepository.existsByEmail(email);
    }

    public boolean isUniqueUsername(String username) {
        return !memberRepository.existsByUsername(username);
    }

    private CodeZapException throwUnauthorized() {
        throw new CodeZapException(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다.");
    }
}
