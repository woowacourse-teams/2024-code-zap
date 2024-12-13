package codezap.auth.controller;

import codezap.auth.dto.LoginAndMemberDto;
import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.LoginResponse;
import codezap.auth.manager.CredentialManager;
import codezap.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements SpringDocAuthController {

    private final CredentialManager credentialManager;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse httpServletResponse
    ) {
        LoginAndMemberDto loginAndMemberDto = authService.login(loginRequest);
        credentialManager.setCredential(httpServletResponse, loginAndMemberDto.member());
        return ResponseEntity.ok(loginAndMemberDto.loginResponse());
    }

    @GetMapping("/login/check")
    public ResponseEntity<Void> checkLogin(HttpServletRequest httpServletRequest) {
        credentialManager.getMember(httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpServletResponse) {
        credentialManager.removeCredential(httpServletResponse);
        return ResponseEntity.noContent().build();
    }
}
