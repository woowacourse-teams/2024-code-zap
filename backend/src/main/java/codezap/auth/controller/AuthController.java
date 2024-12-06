package codezap.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import codezap.auth.dto.LoginAndCredentialDto;
import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.LoginResponse;
import codezap.auth.manager.CredentialManager;
import codezap.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

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
        LoginAndCredentialDto loginAndCredentialDto = authService.login(loginRequest);
        credentialManager.setCredential(httpServletResponse, loginAndCredentialDto.credential());
        return ResponseEntity.ok(loginAndCredentialDto.loginResponse());
    }

    @GetMapping("/login/check")
    @ResponseStatus(HttpStatus.OK)
    public void checkLogin(HttpServletRequest httpServletRequest) {
        credentialManager.getMember(httpServletRequest);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse httpServletResponse) {
        credentialManager.removeCredential(httpServletResponse);
    }
}
