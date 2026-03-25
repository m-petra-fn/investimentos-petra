package br.com.petra.config.observability;

import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.propagation.tracecontext.TraceContextPropagation;
import brave.sampler.Sampler;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BravePropagator;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.urlconnection.URLConnectionSender;

@Configuration
public class BraveTracingConfig {

    @Bean
    URLConnectionSender zipkinSender(@Value("${management.zipkin.tracing.endpoint}") String endpoint) {
        return URLConnectionSender.create(endpoint);
    }

    @Bean
    AsyncZipkinSpanHandler zipkinSpanHandler(URLConnectionSender sender) {
        return AsyncZipkinSpanHandler.create(sender);
    }

    @Bean
    CurrentTraceContext braveCurrentTraceContext() {
        return ThreadLocalCurrentTraceContext.newBuilder()
                .addScopeDecorator(MDCScopeDecorator.get())
                .build();
    }

    @Bean
    Tracing braveTracing(
            @Value("${spring.application.name:demo}") String applicationName,
            @Value("${management.tracing.sampling.probability:1.0}") float samplingProbability,
            CurrentTraceContext currentTraceContext,
            AsyncZipkinSpanHandler zipkinSpanHandler) {
        return Tracing.newBuilder()
                .localServiceName(applicationName)
                .currentTraceContext(currentTraceContext)
                .propagationFactory(TraceContextPropagation.newFactoryBuilder().build())
                .traceId128Bit(true)
                .sampler(Sampler.create(samplingProbability))
                .addSpanHandler(zipkinSpanHandler)
                .build();
    }

    @Bean
    io.micrometer.tracing.CurrentTraceContext currentTraceContext(CurrentTraceContext braveCurrentTraceContext) {
        return new BraveCurrentTraceContext(braveCurrentTraceContext);
    }

    @Bean
    Tracer tracer(Tracing tracing, io.micrometer.tracing.CurrentTraceContext currentTraceContext) {
        return new BraveTracer(tracing.tracer(), currentTraceContext, new BraveBaggageManager());
    }

    @Bean
    Propagator propagator(Tracing tracing) {
        return new BravePropagator(tracing);
    }
}

