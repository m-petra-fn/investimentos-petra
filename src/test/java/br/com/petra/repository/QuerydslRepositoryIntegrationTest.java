package br.com.petra.repository;

import br.com.petra.config.QuerydslConfig;
import br.com.petra.domain.Investimento;
import br.com.petra.domain.MoedaDominio;
import br.com.petra.domain.RendimentoDiario;
import br.com.petra.domain.TipoIndexacaoDominio;
import br.com.petra.domain.TipoInvestimentoDominio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class QuerydslRepositoryIntegrationTest {

    @Autowired
    private InvestimentoRepository investimentoRepository;

    @Autowired
    private RendimentoDiarioRepository rendimentoDiarioRepository;

    @Autowired
    private MoedaDominioRepository moedaDominioRepository;

    @Autowired
    private TipoInvestimentoDominioRepository tipoInvestimentoDominioRepository;

    @Autowired
    private TipoIndexacaoDominioRepository tipoIndexacaoDominioRepository;

    private MoedaDominio moeda;
    private TipoInvestimentoDominio tipoInvestimento;
    private TipoIndexacaoDominio tipoIndexacao;

    @BeforeEach
    void setUp() {
        moeda = moedaDominioRepository.save(criarMoeda());
        tipoInvestimento = tipoInvestimentoDominioRepository.save(criarTipoInvestimento());
        tipoIndexacao = tipoIndexacaoDominioRepository.save(criarTipoIndexacao());
    }

    @Test
    void shouldFindInvestimentoByIdUsingQueryDsl() {
        Investimento investimento = investimentoRepository.saveAndFlush(criarInvestimento("12345678901"));

        var resultado = investimentoRepository.findByIdQueryDsl(investimento.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(investimento.getId());
        assertThat(resultado.get().getCpfInvestidor()).isEqualTo("12345678901");
    }

    @Test
    void shouldFindRendimentosByInvestimentoIdUsingQueryDsl() {
        Investimento investimento = investimentoRepository.saveAndFlush(criarInvestimento("12345678901"));
        Investimento outroInvestimento = investimentoRepository.saveAndFlush(criarInvestimento("98765432100"));

        rendimentoDiarioRepository.saveAndFlush(criarRendimento(investimento, LocalDate.of(2026, 3, 1), "100.00"));
        rendimentoDiarioRepository.saveAndFlush(criarRendimento(investimento, LocalDate.of(2026, 3, 2), "200.00"));
        rendimentoDiarioRepository.saveAndFlush(criarRendimento(outroInvestimento, LocalDate.of(2026, 3, 3), "300.00"));

        Page<RendimentoDiario> pagina = rendimentoDiarioRepository.findByInvestimentoIdQueryDsl(
                investimento.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(pagina.getTotalElements()).isEqualTo(2);
        assertThat(pagina.getContent())
                .extracting(RendimentoDiario::getDataReferencia)
                .containsExactly(LocalDate.of(2026, 3, 2), LocalDate.of(2026, 3, 1));
    }

    @Test
    void shouldFindRendimentosByInvestimentoIdAndPeriodoUsingQueryDsl() {
        Investimento investimento = investimentoRepository.saveAndFlush(criarInvestimento("12345678901"));

        rendimentoDiarioRepository.saveAndFlush(criarRendimento(investimento, LocalDate.of(2026, 3, 1), "100.00"));
        rendimentoDiarioRepository.saveAndFlush(criarRendimento(investimento, LocalDate.of(2026, 3, 5), "200.00"));
        rendimentoDiarioRepository.saveAndFlush(criarRendimento(investimento, LocalDate.of(2026, 3, 10), "300.00"));

        Page<RendimentoDiario> pagina = rendimentoDiarioRepository.findByInvestimentoIdAndPeriodoQueryDsl(
                investimento.getId(),
                LocalDate.of(2026, 3, 2),
                LocalDate.of(2026, 3, 9),
                PageRequest.of(0, 10)
        );

        assertThat(pagina.getTotalElements()).isEqualTo(1);
        assertThat(pagina.getContent())
                .extracting(RendimentoDiario::getDataReferencia)
                .containsExactly(LocalDate.of(2026, 3, 5));
    }

    private MoedaDominio criarMoeda() {
        MoedaDominio entity = new MoedaDominio();
        entity.setCodigo("BRL");
        entity.setDescricao("Real");
        return entity;
    }

    private TipoInvestimentoDominio criarTipoInvestimento() {
        TipoInvestimentoDominio entity = new TipoInvestimentoDominio();
        entity.setCodigo("CDB");
        entity.setDescricao("Certificado de Deposito Bancario");
        return entity;
    }

    private TipoIndexacaoDominio criarTipoIndexacao() {
        TipoIndexacaoDominio entity = new TipoIndexacaoDominio();
        entity.setCodigo("POSFIXADO");
        entity.setDescricao("Pos-fixado");
        return entity;
    }

    private Investimento criarInvestimento(String cpf) {
        Investimento entity = new Investimento();
        entity.setCpfInvestidor(cpf);
        entity.setValorInvestido(new BigDecimal("1000.00"));
        entity.setMoeda(moeda);
        entity.setTipoInvestimento(tipoInvestimento);
        entity.setTipoIndexacao(tipoIndexacao);
        entity.setCreatedAt(LocalDateTime.of(2026, 3, 1, 10, 0));
        entity.setUpdatedAt(LocalDateTime.of(2026, 3, 1, 10, 0));
        return entity;
    }

    private RendimentoDiario criarRendimento(Investimento investimento, LocalDate dataReferencia, String valorRendido) {
        RendimentoDiario entity = new RendimentoDiario();
        entity.setInvestimento(investimento);
        entity.setDataReferencia(dataReferencia);
        entity.setValorRendido(new BigDecimal(valorRendido));
        entity.setIrAtual(new BigDecimal("1.0000"));
        entity.setIofAtual(new BigDecimal("0.5000"));
        entity.setValorLiquido(new BigDecimal(valorRendido).subtract(new BigDecimal("1.50")));
        entity.setCreatedAt(LocalDateTime.of(2026, 3, 1, 10, 0));
        entity.setUpdatedAt(LocalDateTime.of(2026, 3, 1, 10, 0));
        return entity;
    }
}

