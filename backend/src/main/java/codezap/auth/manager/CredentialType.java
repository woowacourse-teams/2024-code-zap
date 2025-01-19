package codezap.auth.manager;

import java.util.Arrays;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;

enum CredentialType {
    AUTHORIZATION_HEADER("Authorization Header"),
    COOKIE("Cookie"),
    ;

    private final String headerValue;

    CredentialType(String headerValue) {
        this.headerValue = headerValue;
    }

    static CredentialType findByHeaderValue(String inputValue) {
        return Arrays.stream(values())
                .filter(credentialType -> credentialType.getHeaderValue().equals(inputValue))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(ErrorCode.UNSUPPORTED_CREDENTIAL_TYPE, "지원하지 않는 인증 타입입니다."));
    }

    public String getHeaderValue() {
        return headerValue;
    }
}
