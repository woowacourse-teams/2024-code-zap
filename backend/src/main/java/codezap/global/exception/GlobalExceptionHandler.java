package codezap.global.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleCodeZapException(CodeZapException codeZapException) {
        log.info("[CodeZapException] {}가 발생했습니다.", codeZapException.getClass().getName(), codeZapException);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                codeZapException.getErrorCode().getHttpStatus(),
                codeZapException.getMessage());
        problemDetail.setProperty("type", codeZapException.getErrorCode().getCode());
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return ResponseEntity.status(codeZapException.getErrorCode().getHttpStatus())
                .body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        BindingResult bindingResult = exception.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        log.info("[MethodArgumentNotValidException] {}가 발생했습니다. \n", exception.getClass().getName(), exception);
        return ResponseEntity.badRequest()
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        String.join("\n", errorMessages))
                );
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> handleException(Exception exception) {
        log.error("[Exception] 예상치 못한 오류 {} 가 발생했습니다.", exception.getClass().getName(), exception);
        return ResponseEntity.internalServerError()
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버에서 예상치 못한 오류가 발생하였습니다.")
                );
    }
}
