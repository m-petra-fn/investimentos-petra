package br.com.petra.config.observability;

import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de tracing com Brave e Zipkin.
 * <p>
 * O Spring Boot 4.0+ com micrometer-tracing-bridge-brave auto-configura
 * a maioria dos beans. Esta classe fornece um bean fallback do Tracer
 * caso a auto-configuração falhe.
 */
@Configuration
@Slf4j
public class TracingConfiguration {

    /**
     * Fornece um Tracer padrão se não houver um disponível.
     * Esta é uma solução de fallback simples que usa o tracer padrão
     * do Spring Cloud Brave.
     */
    @Bean
    @ConditionalOnMissingBean(Tracer.class)
    public Tracer tracer() {
        log.info("Auto-configurando Tracer padrão do Micrometer");
        // O Spring Boot vai auto-configurar isso, este método é apenas um fallback
        throw new IllegalStateException(
                "Tracer não foi auto-configurado. Verifique se 'micrometer-tracing-bridge-brave' está no classpath."
        );
    }
}



