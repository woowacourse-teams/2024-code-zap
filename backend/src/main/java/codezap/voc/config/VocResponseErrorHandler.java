package codezap.voc.config;

import java.io.IOException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VocResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) {
        try {
            HttpStatusCode statusCode = response.getStatusCode();
            return statusCode.isError();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new CodeZapException(ErrorCode.INTERNAL_SERVER_ERROR, "스프레드시트 API 요청에 실패했습니다.");
        }
    }

    @Override
    public void handleError(ClientHttpResponse response) {
        try {
            HttpStatusCode statusCode = response.getStatusCode();
            log.error("{}", statusCode);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        throw new CodeZapException(ErrorCode.INTERNAL_SERVER_ERROR, "스프레드시트 API 요청에 실패했습니다.");
    }
}
