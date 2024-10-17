package codezap.global.logger;

import java.io.IOException;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(2)
public class RequestResponseLogger extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long duration = System.currentTimeMillis() - startTime;

        log.info("[Request] {} {}, 헤더 값: {} \n", request.getMethod(), request.getRequestURI(),
                getHeaderAndValue(requestWrapper));
        log.info("[Response] Status: {}, Duration: {}ms, 헤더 값: {} \n", response.getStatus(), duration,
                getHeaderAndValue(responseWrapper));

        responseWrapper.copyBodyToResponse();
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
