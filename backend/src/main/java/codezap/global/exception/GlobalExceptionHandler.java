package codezap.global.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String PROPERTY_ERROR_CODE = "errorCode";
    private static final String PROPERTY_TIMESTAMP = "timestamp";

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleCodeZapException(CodeZapException codeZapException) {
        log.info("[CodeZapException] {}가 발생했습니다.", codeZapException.getClass().getName(), codeZapException);

        return ResponseEntity.status(codeZapException.getErrorCode().getHttpStatus())
                .body(codeZapException.toProblemDetail());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        log.info("[MethodArgumentNotValidException] {}가 발생했습니다. \n", exception.getClass().getName(), exception);

        BindingResult bindingResult = exception.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        CodeZapException codeZapException =
                new CodeZapException(ErrorCode.INVALID_REQUEST, String.join("\n", errorMessages));

        return ResponseEntity.status(codeZapException.getErrorCode().getHttpStatus())
                .body(codeZapException.toProblemDetail());
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleException(Exception exception) {
        log.error("[Exception] 예상치 못한 오류 {} 가 발생했습니다.", exception.getClass().getName(), exception);
        CodeZapException codeZapException =
                new CodeZapException(ErrorCode.INTERNAL_SERVER_ERROR, "서버에서 예상치 못한 오류가 발생하였습니다.");
        return ResponseEntity.internalServerError()
                .body(codeZapException.toProblemDetail());
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(
            @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        if (body instanceof Exception) {
            problemDetail.setDetail(((Exception) body).getMessage());
        }
        return ResponseEntity.status(statusCode)
                .body(setProperties(problemDetail, ErrorCode.SPRING_GLOBAL_EXCEPTION.getCode()));
    }

    public static ProblemDetail setProperties(ProblemDetail problemDetail, int code) {
        problemDetail.setProperty(PROPERTY_ERROR_CODE, code);
        problemDetail.setProperty(PROPERTY_TIMESTAMP, LocalDateTime.now());

        return problemDetail;
    }
}
