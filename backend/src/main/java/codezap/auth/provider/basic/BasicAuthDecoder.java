package codezap.auth.provider.basic;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicAuthDecoder {

    private static final String BASIC_AUTH_SEPARATOR = ":";
    private static final int BASIC_AUTH_LENGTH = 2;

    public static String[] decodeBasicAuth(String credential) {
        String[] values = decodeBase64(credential).split(BASIC_AUTH_SEPARATOR, BASIC_AUTH_LENGTH);
        validateBasicAuth(values);
        return values;
    }

    private static String decodeBase64(String base64Credentials) {
        try {
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            return new String(credDecoded, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "잘못된 Base64 인코딩입니다.");
        }
    }

    private static void validateBasicAuth(String[] values) {
        if (values.length != BASIC_AUTH_LENGTH || values[0].isEmpty() || values[1].isEmpty()) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "인증 정보가 올바르지 않습니다. 다시 로그인 해주세요.");
        }
    }
}
