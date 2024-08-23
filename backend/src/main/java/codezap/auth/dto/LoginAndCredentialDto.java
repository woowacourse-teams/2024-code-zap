package codezap.auth.dto;

import codezap.auth.dto.response.LoginResponse;

public record LoginAndCredentialDto(
        LoginResponse loginResponse,
        String credential
) {
}
