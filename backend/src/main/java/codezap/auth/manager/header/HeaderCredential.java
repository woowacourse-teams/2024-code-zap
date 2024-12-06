package codezap.auth.manager.header;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;

public record HeaderCredential(String type, String value) {

    private static final String DELIMITER = " ";
    private static final int AUTHORIZATION_TYPE_INDEX = 0;
    private static final int CREDENTIAL_TYPE_INDEX = 1;

    public static HeaderCredential from(String authorizationHeader) {
        String[] typeAndCredential = authorizationHeader.split(DELIMITER);
        if(typeAndCredential.length != 2) {
            throw new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증 헤더의 값이 잘못되었습니다.");
        }
        return new HeaderCredential(typeAndCredential[AUTHORIZATION_TYPE_INDEX], typeAndCredential[CREDENTIAL_TYPE_INDEX]);
    }

    public String toAuthorizationHeader() {
        return type() + DELIMITER + value();
    }
}
