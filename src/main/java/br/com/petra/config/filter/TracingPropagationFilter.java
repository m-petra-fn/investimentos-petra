package br.com.petra.config.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class TracingPropagationFilter extends OncePerRequestFilter {

    private final Tracer tracer;
    private final Propagator propagator;

    public TracingPropagationFilter(Tracer tracer, Propagator propagator) {
        this.tracer = tracer;
        this.propagator = propagator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Span.Builder spanBuilder = propagator.extract(request, HttpServletRequest::getHeader)
                .name(request.getMethod() + " " + request.getRequestURI());

        Span span = spanBuilder.start();
        try (Tracer.SpanInScope ignored = tracer.withSpan(span)) {
            filterChain.doFilter(request, response);
        } finally {
            span.end();
        }
    }
}

