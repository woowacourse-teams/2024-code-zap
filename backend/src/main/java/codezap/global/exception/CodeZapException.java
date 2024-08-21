package codezap.global.exception;

import lombok.Getter;

@Getter
public class CodeZapException extends RuntimeException {
    private final ErrorCode errorCode;

    public CodeZapException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
