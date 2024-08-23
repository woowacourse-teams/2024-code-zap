package codezap.global.logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestResponseLogger extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long duration = System.currentTimeMillis() - startTime;

        log.info("[Request] {} {}, 헤더 값: {} \n 요청 바디: {}", request.getMethod(), request.getRequestURI(),
                getHeaderAndValue(requestWrapper), getBodyAsUtf8String(requestWrapper.getContentAsByteArray()));
        log.info("[Response] Status: {}, Duration: {}ms, 헤더 값: {} \n 응답 바디: {}", response.getStatus(), duration,
                getHeaderAndValue(responseWrapper), getBodyAsUtf8String(responseWrapper.getContentAsByteArray()));

        responseWrapper.copyBodyToResponse();
    }

    private String getBodyAsUtf8String(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String getHeaderAndValue(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder headerAndValue = new StringBuilder();
        requestWrapper.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            String headerValue = requestWrapper.getHeader(headerName);
            headerAndValue.append(headerName).append(" : ").append(headerValue).append("\n");
        });

        return headerAndValue.toString();
    }

    private String getHeaderAndValue(ContentCachingResponseWrapper requestWrapper) {
        return requestWrapper.getHeaderNames().stream().map(headerName -> {
            String headerValue = requestWrapper.getHeader(headerName);
            return headerName + " : " + headerValue;
        }).collect(Collectors.joining("\n"));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/swagger") || path.contains("/v3/api-docs") || path.contains("/actuator/prometheus");
    }
}
