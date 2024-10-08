package codezap.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CodeZapException extends RuntimeException {

    private final ErrorCode errorCode;

    public CodeZapException(HttpStatus httpStatus, String message) {
        this(ErrorCode.of(httpStatus), message);
    }

    public CodeZapException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
