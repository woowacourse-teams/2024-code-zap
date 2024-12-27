package codezap.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.LoginResponse;
import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements SpringDocAuthController {

    private final CredentialManager credentialManager;
    private final CredentialProvider credentialProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse httpServletResponse
    ) {
        var loginMember = authService.login(loginRequest);
        var credential = credentialProvider.createCredential(loginMember);
        credentialManager.setCredential(httpServletResponse, credential);
        return ResponseEntity.ok(LoginResponse.from(loginMember));
    }

    @GetMapping("/login/check")
    public ResponseEntity<Void> checkLogin(HttpServletRequest httpServletRequest) {
        var credential = credentialManager.getCredential(httpServletRequest);
        credentialProvider.extractMember(credential);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpServletResponse) {
        credentialManager.removeCredential(httpServletResponse);
        return ResponseEntity.noContent().build();
    }
}
