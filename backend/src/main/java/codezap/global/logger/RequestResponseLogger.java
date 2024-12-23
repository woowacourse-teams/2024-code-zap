package codezap.global.logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(2)
public class RequestResponseLogger extends OncePerRequestFilter {

    private static final int ERROR_CODE = 500;
    private static final int WARN_CODE = 400;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long duration = System.currentTimeMillis() - startTime;

        logResponse(request, response, requestWrapper, duration, responseWrapper);

        responseWrapper.copyBodyToResponse();
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response,
            ContentCachingRequestWrapper requestWrapper, long duration, ContentCachingResponseWrapper responseWrapper
    ) {
        int status = response.getStatus();
        String requestMessage = String.format(
                "[Request] %s %s, 헤더 값: %s",
                request.getMethod(),
                request.getRequestURI(),
                getHeaderAsJson(requestWrapper));
        String responseMessage = String.format(
                "[Response] status: %d, duration: %dms, headers: %s",
                status,
                duration,
                getHeaderAsJson(responseWrapper));

        logByStatus(status, requestMessage, responseMessage);
    }

    private String getHeaderAsJson(ContentCachingRequestWrapper requestWrapper) {
        Set<String> requiredHeaders = Set.of("origin", "host", "content-type");

        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        Collections.list(headerNames).stream()
                .filter(headerName -> requiredHeaders.contains(headerName.toLowerCase()))
                .forEach(headerName -> headersMap.put(headerName, requestWrapper.getHeader(headerName)));

        return convertMapToJson(headersMap);
    }

    private String getHeaderAsJson(ContentCachingResponseWrapper responseWrapper) {
        Set<String> requiredHeaders = Set.of("docker-hostname");

        Map<String, String> headersMap = new HashMap<>();
        responseWrapper.getHeaderNames().stream()
                .filter(headerName -> requiredHeaders.contains(headerName.toLowerCase()))
                .forEach(headerName -> headersMap.put(headerName, responseWrapper.getHeader(headerName)));

        headersMap.put("docker-hostname", System.getenv("HOSTNAME"));

        return convertMapToJson(headersMap);
    }

    private String convertMapToJson(Map<String, String> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("헤더 정보를 JSON으로 변환하는 중 오류 발생", e);
            return "{}";
        }
    }

    private void logByStatus(int status, String requestMessage, String responseMessage) {
        if (status >= ERROR_CODE) {
            log.error(requestMessage);
            log.error(responseMessage);
            return;
        }
        if (status >= WARN_CODE) {
            log.warn(requestMessage);
            log.warn(responseMessage);
            return;
        }
        log.info(requestMessage);
        log.info(responseMessage);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/swagger") || path.contains("/v3/api-docs") || path.contains("/actuator");
    }
}
