package br.com.petra.web.rest.demo;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObservabilityDemoControllerTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private ObservabilityDemoService observabilityDemoService;

    @Mock
    private Tracer tracer;

    @Mock
    private Span span;

    @Mock
    private TraceContext traceContext;

    @Test
    void testTracingReturnsTraceIdInHeaderAndBody() {
        when(observabilityDemoService.performSimpleOperation()).thenReturn("Operacao OK");
        when(tracer.currentSpan()).thenReturn(span);
        when(span.context()).thenReturn(traceContext);
        when(traceContext.traceId()).thenReturn("trace-123");

        ObservabilityDemoController controller = new ObservabilityDemoController(meterRegistry, observabilityDemoService);
        ReflectionTestUtils.setField(controller, "tracer", tracer);

        ResponseEntity<TraceResponse> response = controller.testTracing();

        assertEquals("trace-123", response.getHeaders().getFirst("X-Trace-Id"));
        assertNotNull(response.getBody());
        assertEquals("Operacao OK", response.getBody().message());
        assertEquals("trace-123", response.getBody().traceId());
    }
}

