package codezap.member.service;

import jakarta.servlet.http.Cookie;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.LoginRequest;
import codezap.member.dto.MemberDto;
import codezap.member.dto.SignupRequest;
import codezap.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final CategoryRepository categoryJpaRepository;

    public Member signup(SignupRequest request) {
        assertUniqueEmail(request.email());
        assertUniqueUsername(request.username());
        Member member = new Member(request.email(), request.password(), request.username());
        categoryJpaRepository.save(Category.createDefaultCategory(member));
        return memberRepository.save(member);
    }

    public MemberDto login(LoginRequest request) {
        return authService.authorizeByEmailAndPassword(request.email(), request.password());
    }

    public void checkLogin(Cookie[] cookies) {
        authService.authorizeByCookie(cookies);
    }

    public void assertUniqueEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "이메일이 이미 존재합니다.");
        }
    }

    public void assertUniqueUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "사용자명이 이미 존재합니다.");
        }
    }
}
