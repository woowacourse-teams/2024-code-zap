package com.codezap.exception;

public class PluginException extends RuntimeException {

    private final ErrorType errorType;
    private int httpStatus;

    public PluginException(ErrorType errorType) {
        super();
        this.errorType = errorType;
    }

    public PluginException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public PluginException(String message, int httpStatus) {
        super(message);
        this.errorType = ErrorType.API_ERROR;
        this.httpStatus = httpStatus;
    }

    public boolean matchErrorType(ErrorType errorType) {
        return this.errorType == errorType;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
