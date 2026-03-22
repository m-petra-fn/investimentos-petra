package br.com.petra.config.filter;

import br.com.petra.config.observability.ActuatorObservationFilterConfig;
import brave.Tracing;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
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

    private static final String FAPI_HEADER_NAME = "x-fapi-interaction-id";
    private static final String ZIPKIN_TAG_FAPI_INTERACTION_ID = "fapi.interaction_id";

    private final Tracing tracing;

    public TracingPropagationFilter(Tracing tracing) {
        this.tracing = tracing;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestPath = request.getRequestURI();
        // Pular tracing para /actuator/** e /swagger-ui** para evitar poluição do Zipkin
        return ActuatorObservationFilterConfig.isFilteredPath(requestPath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        TraceContextOrSamplingFlags extracted = extractIncomingContext(request);
        brave.Span span = tracing.tracer().nextSpan(extracted)
                .name(request.getMethod() + " " + request.getRequestURI())
                .start();

        String fapiInteractionId = request.getHeader(FAPI_HEADER_NAME);
        if (fapiInteractionId != null && !fapiInteractionId.isBlank()) {
            // Tag no span raiz para facilitar query no Zipkin.
            span.tag(ZIPKIN_TAG_FAPI_INTERACTION_ID, fapiInteractionId);
            // Adicionar como baggage remoto para propagação automática
            span.remoteIpAndPort(request.getRemoteAddr(), request.getRemotePort());
        }

        try (var ignored = tracing.currentTraceContext().newScope(span.context())) {
            filterChain.doFilter(request, response);
        } finally {
            span.finish();
        }
    }

    private TraceContextOrSamplingFlags extractIncomingContext(HttpServletRequest request) {
        String traceparent = request.getHeader("traceparent");
        if (traceparent == null || traceparent.isBlank()) {
            return tracing.propagation().extractor(HttpServletRequest::getHeader).extract(request);
        }

        String[] parts = traceparent.trim().split("-");
        if (parts.length != 4) {
            return tracing.propagation().extractor(HttpServletRequest::getHeader).extract(request);
        }

        String traceIdHex = parts[1];
        String parentSpanHex = parts[2];
        String flagsHex = parts[3];

        try {
            // Parse manual do traceparent para garantir continuidade W3C no trace atual.
            long traceIdHigh = traceIdHex.length() == 32 ? Long.parseUnsignedLong(traceIdHex.substring(0, 16), 16) : 0L;
            long traceId = Long.parseUnsignedLong(traceIdHex.length() == 32 ? traceIdHex.substring(16) : traceIdHex, 16);
            long parentSpanId = Long.parseUnsignedLong(parentSpanHex, 16);

            TraceContext.Builder builder = TraceContext.newBuilder()
                    .traceIdHigh(traceIdHigh)
                    .traceId(traceId)
                    .spanId(parentSpanId)
                    .sampled("01".equalsIgnoreCase(flagsHex));

            return TraceContextOrSamplingFlags.create(builder.build());
        } catch (RuntimeException ignored) {
            return tracing.propagation().extractor(HttpServletRequest::getHeader).extract(request);
        }
    }
}

