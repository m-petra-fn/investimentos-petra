# 🔍 Observabilidade e Tracing - Guia Rápido

## 📦 O que foi configurado

✅ **Micrometer** - Coleta de métricas e tracing  
✅ **Prometheus** - Banco de dados de métricas  
✅ **Grafana** - Visualização de dashboards  
✅ **Zipkin** - Rastreamento distribuído  
✅ **Aspecto de Performance** - Rastreamento automático de métodos  

## 🚀 Para começar

### 1. Iniciar Docker Compose
```bash
docker-compose up -d
```

Vai iniciar:
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)
- **Zipkin**: http://localhost:9411

### 2. Compilar e rodar a app
```bash
.\mvnw.cmd spring-boot:run
```

### 3. Testar observabilidade

**Teste simples:**
```bash
curl http://localhost:8080/api/observability-demo/test
```

**Teste com delay (ex: 500ms):**
```bash
curl http://localhost:8080/api/observability-demo/test-with-delay/500
```

**Ver métricas em Prometheus:**
```bash
curl http://localhost:8080/actuator/prometheus
```

## 📊 Visualizações

### Zipkin (Traces)
1. Acesse: http://localhost:9411
2. Clique "Run Query"
3. Veja todos os traces com tempo de execução por camada

### Prometheus (Métricas)
1. Acesse: http://localhost:9090
2. Digite `app_layer_execution` na busca
3. Veja tempos de execução: max, sum, count

### Grafana (Dashboards)
1. Acesse: http://localhost:3000
2. Login: admin/admin
3. Adicione Prometheus como fonte de dados
4. Crie dashboards

## 🎯 Funcionamento

O **PerformanceTracingAspect** intercepta automaticamente:

- ✅ Controllers (`br.com.petra.web.*`)
- ✅ Services (`br.com.petra.service.*`)
- ✅ Repositories (`br.com.petra.repository.*`)

Para cada método, registra:
- ⏱️ Tempo de execução
- 🏷️ Tags de contexto (camada, classe, método)
- 📝 Logs correlacionados com trace ID
- ⚠️ Informações de erro

## 📚 Mais detalhes

Veja `OBSERVABILITY.md` para documentação completa!

## 🆘 Troubleshooting

**Traces não aparecem?**
- Confirme que Zipkin está rodando: `docker ps`
- Verifique `management.zipkin.tracing.endpoint` em `application.properties`

**Métricas não aparecem?**
- Aguarde 5+ segundos (intervalo de scrape)
- Verifique `/actuator/prometheus` manualmente

**Erro de compilação?**
- Rode `.\mvnw.cmd clean compile`

