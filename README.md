# Demo Investimentos REST CRUD

Projeto Spring Boot para testar conceitos com CRUD de investimentos de renda fixa e rendimentos diarios.

## O que ja vem pronto

- CRUD de `Investimento` com paginacao (`Page`)
- CRUD de `RendimentoDiario` com paginacao geral e por investimento
- Tabelas de dominio:
    - `tb_moeda`
    - `tb_tipo_investimento`
    - `tb_tipo_indexacao`
- Auditoria automatica com `@CreatedDate` e `@LastModifiedDate`
- DTOs `record` com datas e valores numericos retornados como `String`
- Mapeamento entidade <-> DTO com MapStruct
- Banco H2 em memoria com criacao automatica do schema

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

