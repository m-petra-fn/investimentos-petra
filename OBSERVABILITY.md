# Guia de Observabilidade e Tracing - Projeto Demo

## 🎯 Visão Geral

Este projeto está configurado com **Micrometer** para observabilidade completa, incluindo:

- **Métricas**: Monitoramento em tempo real (Prometheus)
- **Tracing Distribuído**: Rastreamento de requisições (Zipkin)
- **Health Checks**: Verificação de saúde da aplicação

## 📊 Stack de Observabilidade

### Componentes

1. **Micrometer Registry (Prometheus)**
   - Coleta e exporta métricas da aplicação
   - Endpoint: `http://localhost:8080/actuator/prometheus`

2. **Micrometer Tracing (Brave + Zipkin)**
   - Rastreamento distribuído de requisições
   - Visualização no Zipkin: `http://localhost:9411`

3. **Prometheus**
   - Banco de dados de séries temporais
   - Console: `http://localhost:9090`

4. **Grafana**
   - Dashboard de visualização
   - Acesso: `http://localhost:3000` (admin/admin)

5. **Zipkin**
   - UI para visualizar traces distribuídos
   - Console: `http://localhost:9411`

## 🚀 Como Usar

### Passo 1: Iniciar os Containers Docker

```bash
docker-compose up -d
```

Isso iniciará:
- Prometheus (porta 9090)
- Grafana (porta 3000)
- Zipkin (porta 9411)

### Passo 2: Iniciar a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### Passo 3: Testar a Observabilidade

#### Teste Simples

```bash
curl http://localhost:8080/api/observability-demo/test
```

#### Teste com Delay Customizável

```bash
# Simular operação que leva 500ms
curl http://localhost:8080/api/observability-demo/test-with-delay/500
```

#### Verificar Métricas

```bash
curl http://localhost:8080/actuator/prometheus
```

#### Ver Informações de Trace

```bash
curl http://localhost:8080/api/observability-demo/trace-info
```

#### Contar Métricas Registradas

```bash
curl http://localhost:8080/api/observability-demo/metrics-count
```

## 📈 Visualizar Dados

### Prometheus
1. Abra `http://localhost:9090`
2. Vá para "Graph"
3. Procure por `app_layer_execution` para ver métricas de tempo de execução
4. Escolha valores como:
   - `app_layer_execution_seconds_max` - Tempo máximo
   - `app_layer_execution_seconds_count` - Número de execuções
   - `app_layer_execution_seconds_sum` - Tempo total

### Zipkin
1. Abra `http://localhost:9411`
2. Clique em "Run Query"
3. Veja todos os traces distribuídos
4. Clique em um trace para ver os detalhes:
   - Tempo total da requisição
   - Breakdown de cada camada (controller, service, etc)
   - Logs correlacionados com o trace

### Grafana
1. Abra `http://localhost:3000`
2. Login: admin/admin
3. Vá para "Data Sources" e adicione Prometheus como fonte
4. Crie dashboards personalizados com as métricas

## 🔍 Entendendo o Rastreamento Automático

### Aspecto de Performance (PerformanceTracingAspect)

O aspecto `PerformanceTracingAspect` intercepta automaticamente:

- Todos os métodos em `br.com.petra.web.*` (Controllers)
- Todos os métodos em `br.com.petra.service.*` (Services)
- Todos os métodos em `br.com.petra.repository.*` (Repositories)

Para cada método, registra:
- **Tempo de execução** em nanosegundos
- **Trace distribuído** com correlação de IDs
- **Tags de contexto** (camada, classe, método)
- **Informações de erro** em caso de exceção

### Logs com Correlação de Trace

Todos os logs incluem correlação de trace:

```
[demo,trace-id-123,span-id-456] - Seu log aqui
```

Isso permite rastrear todos os logs de uma requisição mesmo em sistemas distribuídos.

## 📋 Configuração (application.properties)

```properties
# Exposição de endpoints de monitoramento
management.endpoints.web.exposure.include=health,info,metrics,prometheus

# Saúde detalhada
management.endpoint.health.show-details=always

# 100% de sampling para desenvolvimento (ajuste em produção)
management.tracing.sampling.probability=1.0

# Histograma de percentis para app.layer.execution
management.metrics.distribution.percentiles-histogram.app.layer.execution=true

# Tags globais de métricas
management.metrics.tags.application=${spring.application.name}

# Endpoint do Zipkin
management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans

# Habilitar Spring Cloud Sleuth (auto-configuração)
spring.sleuth.enabled=true

# Padrão de logs com correlação de trace
logging.pattern.correlation=[${spring.application.name:},%X{traceId:-},%X{spanId:-}]
```

## 💡 Dicas Práticas

### 1. Investigar Lentidão

Se uma requisição está lenta:
1. Vá ao Zipkin
2. Procure pelo trace da requisição
3. Veja qual camada levou mais tempo
4. Analise as métricas no Prometheus/Grafana

### 2. Adicionar Rastreamento Customizado

Se precisar rastrear um método específico fora das camadas padrão:

```java
@Component
public class MeuServicoCustomizado {
    private final Tracer tracer;
    
    public void metodoCustomizado() {
        var span = tracer.nextSpan().name("meuServicoCustomizado").start();
        try (var _ignored = tracer.withSpan(span)) {
            span.tag("operacao", "customizada");
            // seu código aqui
        }
    }
}
```

### 3. Filtrar Traces

No Zipkin, você pode filtrar por:
- Service name: "demo"
- Min duration: "500ms"
- Tags: "error=true"

## 🛠️ Troubleshooting

### Traces não aparecem no Zipkin
- Verifique se `management.zipkin.tracing.endpoint` está correto
- Verifique se o container Zipkin está rodando: `docker ps`
- Verifique os logs da aplicação

### Métricas não aparecem no Prometheus
- Verifique se o container Prometheus está rodando
- Confirme que a aplicação está respondendo em `/actuator/prometheus`
- Verifique se o intervalo de scrape (5s no compose) passou

### Alto consumo de memória
- Reduzir `management.tracing.sampling.probability` para produção
- Por padrão está em 1.0 (100%) para facilitar desenvolvimento
- Recomendado: 0.1 (10%) para produção

## 📚 Referências

- [Micrometer Documentation](https://micrometer.io/)
- [Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
- [Spring Cloud Sleuth](https://spring.io/projects/spring-cloud-sleuth)
- [Zipkin Documentation](https://zipkin.io/)

## 🎓 Próximos Passos

1. Explorar as métricas disponíveis em `/actuator/metrics`
2. Criar dashboards customizados no Grafana
3. Configurar alertas baseados em limites de performance
4. Ajustar sampling para produção (reduzir de 100% para 10-25%)

