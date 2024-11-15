package codezap.voc.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.voc.dto.VocRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VocService {

    private final RestClient restClient;

    public VocService(RestClient.Builder builder) {
        restClient = builder.build();
    }

    public void contact(VocRequest request) {
        restClient.post()
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientErrorHandler())
                .onStatus(HttpStatusCode::is5xxServerError, serverErrorHandler());
        // TODO: timeout 관련 예외 처리
    }

    public ErrorHandler clientErrorHandler() {
        return (request, response) -> {
            log.error("Client error occurred: {}", response.getBody());
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "외부 API 요청에 실패했습니다.");
        };
    }

    public ErrorHandler serverErrorHandler() {
        return (request, response) -> {
            log.error("Server error occurred: {}", response.getBody());
            throw new CodeZapException(ErrorCode.INTERNAL_SERVER_ERROR, "외부 API 요청에 실패했습니다.");
        };
    }
}
