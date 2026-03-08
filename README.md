# Demo Investimentos REST CRUD

Projeto Spring Boot para testar conceitos com CRUD de investimentos de renda fixa e rendimentos diarios.

## O que ja vem pronto

- CRUD de `Investimento` com paginacao (`Page`)
- CRUD de `RendimentoDiario` com paginacao geral e por investimento
- Tabelas de dominio:
    - `tb_moeda` (1x BRL)
    - `tb_tipo_investimento` (4x: CDB, LCI, LCA, TESOURO)
    - `tb_tipo_indexacao` (2x: PREFIXADO, POSFIXADO)
- **25 investimentos com 50 rendimentos cada** (1.250 rendimentos) gerados automaticamente ao iniciar
- Auditoria automatica com `@CreatedDate` e `@LastModifiedDate`
- DTOs `record` com datas e valores numericos retornados como `String`
- Mapeamento entidade <-> DTO com MapStruct
- Banco H2 em memoria com criacao automatica do schema

## Inicialização de Dados

Ao subir a aplicação, executa-se em ordem:

1. **@Order(1) - DomainDataInitializer**
    - Cria dados de dominio (moedas, tipos de investimento, tipos de indexação)
    - Verifica se ja existem antes de criar (idempotente)
    - Log: "✓ Dados de dominio carregados com sucesso!"

2. **@Order(2) - FakeDataInitializer**
    - Cria 25 investimentos aleatorios (valores, CPFs, tipos variados)
    - Cria 50 rendimentos para cada investimento (datas, valores, IR, IOF)
    - Calcula valor liquido automaticamente
    - Verifica se ja existem investimentos (pula criação se houver)
    - Log: "✓ 25 investimentos e 1250 rendimentos criados com sucesso!"

## Endpoints principais

- `POST /api/investimentos`
- `GET /api/investimentos`
- `GET /api/investimentos/{id}`
- `PUT /api/investimentos/{id}`
- `DELETE /api/investimentos/{id}`

- `POST /api/investimentos/{investimentoId}/rendimentos`
- `GET /api/rendimentos`
- `GET /api/rendimentos/{id}`
- `PUT /api/rendimentos/{id}`
- `DELETE /api/rendimentos/{id}`
- `GET /api/investimentos/{investimentoId}/rendimentos`

## Executar

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

## Console H2

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:investimentosdb`
- User: `sa`
- Password: (vazio)

## Swagger/OpenAPI

- URL: `http://localhost:8080/swagger-ui.html`

