package codezap.global.exception;

import org.springframework.http.ProblemDetail;

import lombok.Getter;

@Getter
public class CodeZapException extends RuntimeException {

    private final ErrorCode errorCode;

    public CodeZapException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                errorCode.getHttpStatus(),
                getMessage());
        return GlobalExceptionHandler.setProperties(problemDetail, errorCode.getCode());
    }
}
