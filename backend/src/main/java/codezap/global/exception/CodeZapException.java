package codezap.global.exception;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public class CodeZapException extends RuntimeException {
    private final HttpStatusCode httpStatusCode;

    public CodeZapException(HttpStatusCode httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
