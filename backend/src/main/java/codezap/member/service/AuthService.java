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
import codezap.member.dto.MemberDto;
import codezap.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public MemberDto authorizeByEmailAndPassword(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(this::throwUnauthorized);
        if (!member.matchPassword(password)) {
            throwUnauthorized();
        }
        return MemberDto.from(member);
    }

    public MemberDto authorizeByCookie(Cookie[] cookies) {
        String authHeaderValue = getAuthCookieValue(cookies);
        String[] credentials = decodeCredentials(authHeaderValue);
        String email = credentials[0];
        String password = credentials[1];
        return authorizeByEmailAndPassword(email, password);
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

    private CodeZapException throwUnauthorized() {
        throw new CodeZapException(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다.");
    }
}
