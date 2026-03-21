package br.com.petra.config.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Objects;

@Component
public class TimingFilter implements Filter {

    @Autowired(required = false)
    private Tracer tracer;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);
        long startTime = System.currentTimeMillis();

        chain.doFilter(request, wrappedResponse);

        long responseTime = System.currentTimeMillis() - startTime;
        wrappedResponse.addHeader("X-Response-Time-Ms", Objects.toString(responseTime));
        Span currentSpan = tracer != null ? tracer.currentSpan() : null;
        String traceId = currentSpan != null && currentSpan.context() != null
                ? currentSpan.context().traceId()
                : null;
        if (traceId != null) {
            wrappedResponse.addHeader("X-Trace-Id", traceId);
        }
        wrappedResponse.copyBodyToResponse();
    }
}
