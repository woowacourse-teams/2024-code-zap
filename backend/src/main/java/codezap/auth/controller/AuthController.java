package codezap.auth.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import codezap.auth.configuration.AuthenticationPrinciple;
import codezap.auth.dto.Credential;
import codezap.auth.dto.LoginMember;
import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.LoginResponse;
import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.auth.service.AuthService;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements SpringDocAuthController {

    private final List<CredentialManager> credentialManagers;
    private final CredentialProvider credentialProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse httpServletResponse
    ) {
        LoginMember loginMember = authService.login(loginRequest);
        Credential credential = credentialProvider.createCredential(loginMember);
        credentialManagers.forEach(
                credentialManager -> credentialManager.setCredential(httpServletResponse, credential)
        );
        return ResponseEntity.ok(LoginResponse.from(loginMember));
    }

    @GetMapping("/login/check")
    public ResponseEntity<Void> checkLogin(
            @AuthenticationPrinciple Member member,
            HttpServletRequest httpServletRequest
    ) {
        if (member == null) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증 정보가 없습니다. 다시 로그인해 주세요.");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpServletResponse) {
        credentialManagers.forEach(
                credentialManager -> credentialManager.removeCredential(httpServletResponse)
        );
        return ResponseEntity.noContent().build();
    }
}
