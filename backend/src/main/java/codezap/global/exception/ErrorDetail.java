package codezap.global.exception;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.http.ProblemDetail;

public record ErrorDetail(
        int type,
        int status,
        String detail,
        URI instance,
        LocalDateTime timestamp
) {
    public static ErrorDetail of(ErrorCode errorCode, String message) {
        return new ErrorDetail(
                errorCode.getCode(),
                errorCode.getHttpStatus().value(),
                message,
                ProblemDetail.forStatus(errorCode.getHttpStatus()).getInstance(),
                LocalDateTime.now()
        );
    }
}
