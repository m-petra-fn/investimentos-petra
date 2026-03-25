package br.com.petra.config.observability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LayerTimingAspect {

    private final ObservationRegistry observationRegistry;

    public LayerTimingAspect(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @Around("execution(* br.com.petra.web..*(..))")
    public Object timeWebLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        return timeExecution("web", joinPoint);
    }

    @Around("execution(* br.com.petra.service..*(..))")
    public Object timeServiceLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        return timeExecution("service", joinPoint);
    }

    @Around("execution(* br.com.petra.repository..*(..))")
    public Object timeRepositoryLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        return timeExecution("repository", joinPoint);
    }

    private Object timeExecution(String layer, ProceedingJoinPoint joinPoint) throws Throwable {
        String status = "success";
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getMethod().getName();

        Observation observation = Observation.createNotStarted("app.layer.execution", observationRegistry)
                .contextualName(layer + "." + className + "." + methodName)
                .lowCardinalityKeyValue("layer", layer)
                .lowCardinalityKeyValue("class", className)
                .lowCardinalityKeyValue("method", methodName)
                .start();

        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            status = "error";
            observation.error(throwable);
            throw throwable;
        } finally {
            observation.lowCardinalityKeyValue("status", status);
            observation.stop();
        }
    }
}




