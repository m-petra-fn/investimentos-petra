package br.com.petra.config.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Aspecto para rastreamento automático de performance de métodos.
 * 
 * Registra métricas de tempo de execução e cria spans de tracing distribuído
 * para todas as camadas: controllers, services e repositories.
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PerformanceTracingAspect {

    private final MeterRegistry meterRegistry;
    private final Tracer tracer;

    /**
     * Rastreia tempo de execução de métodos em Controllers
     */
    @Around("execution(* br.com.petra.web..*(..))")
    public Object traceControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return traceMethodExecution(joinPoint, "controller");
    }

    /**
     * Rastreia tempo de execução de métodos em Services
     */
    @Around("execution(* br.com.petra.service..*(..))")
    public Object traceServiceExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return traceMethodExecution(joinPoint, "service");
    }

    /**
     * Rastreia tempo de execução de métodos em Repositories
     */
    @Around("execution(* br.com.petra.repository..*(..))")
    public Object traceRepositoryExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return traceMethodExecution(joinPoint, "repository");
    }

    /**
     * Método genérico para rastreamento de execução
     */
    private Object traceMethodExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String operationName = className + "." + methodName;

        // Criar um novo span para este método
        var span = tracer.nextSpan().name(operationName);

        try (var _ignored = tracer.withSpan(span.start())) {
            // Registrar tags de contexto
            span.tag("layer", layer)
                    .tag("class", className)
                    .tag("method", methodName);

            // Medir o tempo de execução
            Timer timer = Timer.builder("app.layer.execution")
                    .description("Tempo de execução de métodos por camada")
                    .tags(Tags.of(
                            Tag.of("layer", layer),
                            Tag.of("class", className),
                            Tag.of("method", methodName)
                    ))
                    .register(meterRegistry);

            long startTime = System.nanoTime();
            try {
                Object result = joinPoint.proceed();
                long duration = System.nanoTime() - startTime;
                timer.record(duration, java.util.concurrent.TimeUnit.NANOSECONDS);

                log.debug("{}.{} executado em {} ms", className, methodName, duration / 1_000_000.0);
                span.tag("success", "true");

                return result;
            } catch (Exception e) {
                long duration = System.nanoTime() - startTime;
                timer.record(duration, java.util.concurrent.TimeUnit.NANOSECONDS);
                span.tag("success", "false")
                        .tag("error", e.getClass().getSimpleName())
                        .event("exception");

                log.error("{}.{} falhou após {} ms: {}", className, methodName, 
                        duration / 1_000_000.0, e.getMessage());
                throw e;
            }
        }
    }
}

