package br.com.petra.web.rest.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço de demonstração de observabilidade.
 * 
 * Este serviço demonstra como as métricas são registradas automaticamente
 * através do aspecto PerformanceTracingAspect.
 */
@Service
@Slf4j
public class ObservabilityDemoService {

    /**
     * Operação simples que será rastreada automaticamente
     */
    public String performSimpleOperation() {
        log.info("Executando operação simples");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Operação simples concluída";
    }

    /**
     * Operação com delay customizável
     */
    public String performOperationWithDelay(long delayMs) {
        log.info("Executando operação com delay de {} ms", delayMs);
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return String.format("Operação com delay concluída após %d ms", delayMs);
    }

    /**
     * Operação que simula processamento complexo em múltiplas etapas
     */
    public String performComplexOperation() {
        log.info("Iniciando operação complexa");
        
        // Etapa 1
        etapa1();
        
        // Etapa 2
        etapa2();
        
        // Etapa 3
        etapa3();
        
        return "Operação complexa concluída com sucesso";
    }

    private void etapa1() {
        log.info("Executando etapa 1");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void etapa2() {
        log.info("Executando etapa 2");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void etapa3() {
        log.info("Executando etapa 3");
        try {
            Thread.sleep(75);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

