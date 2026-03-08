Oi! Eu quero fazer desse um projeto simples para eu testar coisas e conceitos num ambiente Spring REST CRUD. Pode
começar montando um sistema CRUD simples para mim com o seguinte:

Um banco de dados que guarda investimentos de renda fixa.

- Uma tabela para os investimentos, com id do investimento (uuid), valor investido, moeda (por enquanto somente BRL)
  data de criação, data de alteração, tipo de investimento (CDB, LCI, LCA, etc), cpf do investidor (não precisa
  controlar cpfs, pode ser só um campo nessa tabela mesmo).
- Tabelas dominios para os tipos (moeda, tipo de investimento, tipo pos ou pre fixado, )
- Tabela para rendimento diario do investimento, com FK para id do investimento, valor rendido, data, IR atual, IOF
  atual, além de outros campos necessários.
- Use anotação @CreatedDate e @LastModifiedDate para os registros e @EnableJpaAuditing.
- Estou usando H2, então pode criar as entidades para que o banco seja criado ao carregar o sistema.
- Crie endpoints básicos CRUD para adicionar, editar, listar (todos ou por id) para os investimentos e também para as
  tabelas de rendimentos (arquivos separados InvestimentoResource e RendimentoResource, com suas services e repositories
  também)
- Crie DTOs para representar os objetos JSON, crie uma classe mapper (usando mapstruct) aonde é feita a conversão da
  entidade de BD para o DTO.
- Utilize Page do spring para paginar os investimentos e rendimentos de um investimento.
- Utilize Lombok quando aplicável.
- Utilize Records para os DTOs.
- Utilize BigDecimal para valores numericos.
- No DTO, devolva valores numéricos como String e datas como String formatadas.