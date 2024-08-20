package codezap.global.logger;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MDCFilter implements Filter {
    private final String CORRELATION_ID = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        MDC.put(CORRELATION_ID, generateCorrelationId());
        chain.doFilter(request, response);
        MDC.clear();
    }

    private String generateCorrelationId() {
        return UUID.randomUUID()
                .toString()
                .substring(0, 8);
    }
}
