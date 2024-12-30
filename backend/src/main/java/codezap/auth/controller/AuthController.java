package codezap.auth.controller;

import codezap.auth.dto.LoginMember;
import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.LoginResponse;
import codezap.auth.dto.Credential;
import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
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
    private final CredentialProvider credentialProvider;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse httpServletResponse
    ) {
        LoginMember loginMember = authService.login(request);
        Credential credential = credentialProvider.createCredential(loginMember);
        credentialManager.setCredential(httpServletResponse, credential);
        return ResponseEntity.ok(LoginResponse.from(loginMember));
    }

    @GetMapping("/login/check")
    public ResponseEntity<Void> checkLogin(HttpServletRequest httpServletRequest) {
        Credential credential = credentialManager.getCredential(httpServletRequest);
        credentialProvider.extractMember(credential);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpServletResponse) {
        credentialManager.removeCredential(httpServletResponse);
        return ResponseEntity.noContent().build();
    }
}
