package codezap.auth.controller;

import jakarta.validation.Valid;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import codezap.auth.dto.request.OAuthSignupRequest;
import codezap.auth.dto.request.OAuthUniqueCheckRequest;
import codezap.auth.dto.response.OAuthUniqueCheckResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OAuthController implements SpringDocOAuthController {

    @Override
    public ResponseEntity<OAuthUniqueCheckResponse> checkUnique(@Valid @RequestBody OAuthUniqueCheckRequest request) {
        throw new NotImplementedException();
    }

    @Override
    public ResponseEntity<Void> signup(@Valid @RequestBody OAuthSignupRequest request) {
        throw new NotImplementedException();
    }
}
