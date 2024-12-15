package codezap.auth.dto;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;

public record Credential(String type, String value) {

    private static final String DELIMITER = " ";
    private static final int AUTHORIZATION_TYPE_INDEX = 0;
    private static final int CREDENTIAL_TYPE_INDEX = 1;
    private static final int CREDENTIAL_LENGTH = 2;

    public static Credential from(String authorizationHeader) {
        String[] typeAndCredential = authorizationHeader.split(DELIMITER);
        if (typeAndCredential.length != CREDENTIAL_LENGTH) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증 헤더의 값이 잘못되었습니다.");
        }
        return new Credential(typeAndCredential[AUTHORIZATION_TYPE_INDEX], typeAndCredential[CREDENTIAL_TYPE_INDEX]);
    }

    public static Credential basic(String credential) {
        return new Credential("Basic", credential);
    }

    public String toAuthorizationHeader() {
        return type + DELIMITER + value;
    }
}
