package codezap.global.exception;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_MESSAGE_FORMAT(1101, HttpStatus.BAD_REQUEST),
    INVALID_HEADER_OPTION(1102, HttpStatus.BAD_REQUEST),
    INVALID_ID_REQUEST(1103, HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_REQUEST(1104, HttpStatus.BAD_REQUEST),
    INVALID_TEMPLATE_REQUEST(1105, HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_REQUEST(1106, HttpStatus.BAD_REQUEST),
    INVALID_TAG_REQUEST(1107, HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_TEMPLATES(1108, HttpStatus.BAD_REQUEST),
    DEFAULT_CATEGORY(1109, HttpStatus.BAD_REQUEST),

    ENDPOINT_NOT_FOUND(1201, HttpStatus.NOT_FOUND),
    RESOURCE_NOT_FOUND(1202, HttpStatus.NOT_FOUND),
    DUPLICATE_ID(1203, HttpStatus.CONFLICT),
    DUPLICATE_CATEGORY(1204, HttpStatus.CONFLICT),

    UNAUTHORIZED_USER(1301, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ID(1302, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_PASSWORD(1303, HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ACCESS(1304, HttpStatus.FORBIDDEN),

    INTERNAL_SERVER_ERROR(2000, HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final HttpStatus httpStatus;

    public static ErrorCode of(HttpStatus httpStatus) {
        return Arrays.stream(values())
                .filter(value -> value.httpStatus.equals(httpStatus))
                .findAny()
                .orElseThrow();
    }
}
