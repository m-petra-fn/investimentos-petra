package br.com.petra.web.rest.demo;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired(required = false)
    private Tracer tracer;

    @GetMapping("/test")
    public String testTracing() {
        log.info("Iniciando teste de tracing");
        return observabilityDemoService.performSimpleOperation();
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
        String traceId = tracer.currentSpan() != null ?
                tracer.currentSpan().context().traceId() : "N/A";
        String spanId = tracer.currentSpan() != null ?
                tracer.currentSpan().context().spanId() : "N/A";

        String info = String.format("Trace ID: %s, Span ID: %s", traceId, spanId);
        log.info(info);
        return info;
    }
}

