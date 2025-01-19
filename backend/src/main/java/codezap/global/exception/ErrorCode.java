package codezap.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SPRING_GLOBAL_EXCEPTION(1000, HttpStatus.BAD_REQUEST),

    INVALID_REQUEST(1101, HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_TEMPLATES(1102, HttpStatus.BAD_REQUEST),
    DEFAULT_CATEGORY(1103, HttpStatus.BAD_REQUEST),

    RESOURCE_NOT_FOUND(1201, HttpStatus.NOT_FOUND),
    DUPLICATE_ID(1202, HttpStatus.CONFLICT),
    DUPLICATE_CATEGORY(1203, HttpStatus.CONFLICT),

    UNAUTHORIZED_USER(1301, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_ID(1302, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_PASSWORD(1303, HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ACCESS(1304, HttpStatus.FORBIDDEN),
    UNSUPPORTED_CREDENTIAL_TYPE(1304, HttpStatus.BAD_REQUEST),

    INTERNAL_SERVER_ERROR(2000, HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final HttpStatus httpStatus;
}
