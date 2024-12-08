package codezap.voc.config;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VocClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        try {
            return execution.execute(request, body);
        } catch (SocketTimeoutException e) {
            throw new CodeZapException(ErrorCode.INTERNAL_SERVER_ERROR, "스프레드시트 API 요청 시간이 초과되었습니다: " + e.getMessage());
        } catch (IOException e) {
            throw new CodeZapException(ErrorCode.INTERNAL_SERVER_ERROR, "스프레드시트 API 요청에 실패했습니다: " + e.getMessage());
        }
    }
}
