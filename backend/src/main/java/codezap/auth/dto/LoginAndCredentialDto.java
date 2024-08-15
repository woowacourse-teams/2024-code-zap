package codezap.auth.dto;

import codezap.auth.dto.response.LoginResponse;

public record LoginAndCredentialDto(
        LoginResponse loginResponse,
        String credential
) {
    public static LoginAndCredentialDto from(LoginResponse loginResponse, String credential) {
        return new LoginAndCredentialDto(loginResponse, credential);
    }
}
