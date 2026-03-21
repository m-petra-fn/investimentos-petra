package br.com.petra.web.rest.demo;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador de exemplo para demonstrar observabilidade e tracing.
 * <p>
 * Endpoints para testar:
 * - GET /api/observability-demo/test - Testa tracing simples
 * - GET /api/observability-demo/test-with-delay/{delayMs} - Testa com delay customizável
 * - GET /api/observability-demo/metrics - Retorna métricas disponíveis
 */
@RestController
@RequestMapping("/api/observability-demo")
@Slf4j
@RequiredArgsConstructor
public class ObservabilityDemoController {

    private final MeterRegistry meterRegistry;
    private final ObservabilityDemoService observabilityDemoService;
    private final Tracer tracer;

    @GetMapping("/test")
    public ResponseEntity<TraceResponse> testTracing() {
        log.info("Iniciando teste de tracing");
        String result = observabilityDemoService.performSimpleOperation();
        String traceId = resolveTraceId();

        return ResponseEntity.ok()
                .header("X-Trace-Id", traceId)
                .body(new TraceResponse(result, traceId));
    }

    @GetMapping("/test-with-delay/{delayMs}")
    public String testTracingWithDelay(@PathVariable long delayMs) {
        log.info("Iniciando teste de tracing com delay de {} ms", delayMs);
        return observabilityDemoService.performOperationWithDelay(delayMs);
    }

    @GetMapping("/metrics-count")
    public String getMetricsCount() {
        int metricsCount = meterRegistry.getMeters().size();
        log.info("Total de métricas registradas: {}", metricsCount);
        return String.format("Total de métricas: %d", metricsCount);
    }

    @GetMapping("/trace-info")
    public String getTraceInfo() {
        String traceId = resolveTraceId();
        String spanId = resolveSpanId();

        String info = String.format("Trace ID: %s, Span ID: %s", traceId, spanId);
        log.info(info);
        return info;
    }

    @GetMapping("/trace-diagnostics")
    public Map<String, String> getTraceDiagnostics(HttpServletRequest request) {
        Span currentSpan = tracer.currentSpan();
        Map<String, String> diagnostics = new LinkedHashMap<>();
        diagnostics.put("tracerBean", tracer.getClass().getName());
        diagnostics.put("hasCurrentSpan", Boolean.toString(currentSpan != null));
        diagnostics.put("traceId", currentSpan != null && currentSpan.context() != null ? currentSpan.context().traceId() : "N/A");
        diagnostics.put("spanId", currentSpan != null && currentSpan.context() != null ? currentSpan.context().spanId() : "N/A");
        diagnostics.put("incomingTraceparent", valueOrNA(request.getHeader("traceparent")));
        diagnostics.put("incomingB3", valueOrNA(request.getHeader("b3")));
        return diagnostics;
    }

    private String resolveTraceId() {
        Span currentSpan = tracer.currentSpan();
        return currentSpan != null && currentSpan.context() != null
                ? currentSpan.context().traceId()
                : "N/A";
    }

    private String resolveSpanId() {
        Span currentSpan = tracer.currentSpan();
        return currentSpan != null && currentSpan.context() != null
                ? currentSpan.context().spanId()
                : "N/A";
    }

    private String valueOrNA(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }
}

