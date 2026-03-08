package br.com.petra.config;

import br.com.petra.domain.*;
import br.com.petra.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FakeDataInitializer {

    @Bean
    @Order(2)
    public CommandLineRunner loadFakeData(
            InvestimentoRepository investimentoRepository,
            RendimentoDiarioRepository rendimentoDiarioRepository,
            MoedaDominioRepository moedaDominioRepository,
            TipoInvestimentoDominioRepository tipoInvestimentoDominioRepository,
            TipoIndexacaoDominioRepository tipoIndexacaoDominioRepository
    ) {
        return args -> {
            // Verifica se ja existem dados
            if (investimentoRepository.count() > 0) {
                log.info("Dados fake ja existem. Pulando criacao.");
                return;
            }

            log.info("Criando 25 investimentos com 50 rendimentos cada...");

            MoedaDominio moedaBRL = moedaDominioRepository.findByCodigo("BRL")
                    .orElseThrow(() -> new RuntimeException("Moeda BRL nao encontrada"));

            List<TipoInvestimentoDominio> tiposInvestimento = tipoInvestimentoDominioRepository.findAll();
            List<TipoIndexacaoDominio> tiposIndexacao = tipoIndexacaoDominioRepository.findAll();

            Random random = new Random(42); // Seed para reproducibilidade

            List<Investimento> investimentos = new ArrayList<>();
            List<RendimentoDiario> rendimentos = new ArrayList<>();

            for (int i = 1; i <= 25; i++) {
                Investimento investimento = criarInvestimento(
                        i,
                        moedaBRL,
                        tiposInvestimento.get(random.nextInt(tiposInvestimento.size())),
                        tiposIndexacao.get(random.nextInt(tiposIndexacao.size())),
                        random
                );
                investimentos.add(investimento);

                for (int j = 1; j <= 50; j++) {
                    RendimentoDiario rendimento = criarRendimentoDiario(
                            investimento,
                            j,
                            random
                    );
                    rendimentos.add(rendimento);
                }
            }

            investimentoRepository.saveAll(investimentos);
            rendimentoDiarioRepository.saveAll(rendimentos);

            log.info("✓ {} investimentos e {} rendimentos criados com sucesso!",
                    investimentos.size(),
                    rendimentos.size()
            );
        };
    }

    private Investimento criarInvestimento(
            int numero,
            MoedaDominio moeda,
            TipoInvestimentoDominio tipoInvestimento,
            TipoIndexacaoDominio tipoIndexacao,
            Random random
    ) {
        Investimento investimento = new Investimento();
        investimento.setValorInvestido(gerarBigDecimal(1000, 100000, random));
        investimento.setCpfInvestidor(gerarCPFFake(numero));
        investimento.setMoeda(moeda);
        investimento.setTipoInvestimento(tipoInvestimento);
        investimento.setTipoIndexacao(tipoIndexacao);
        return investimento;
    }

    private RendimentoDiario criarRendimentoDiario(
            Investimento investimento,
            int numero,
            Random random
    ) {
        RendimentoDiario rendimento = new RendimentoDiario();
        rendimento.setInvestimento(investimento);
        rendimento.setDataReferencia(LocalDate.now().minusDays(50 - numero));
        rendimento.setValorRendido(gerarBigDecimal(10, 500, random));
        rendimento.setIrAtual(gerarBigDecimal(0, 15, random, 2));
        rendimento.setIofAtual(gerarBigDecimal(0, 5, random, 2));

        // Calcula valor liquido = rendido - (rendido * ir/100) - (rendido * iof/100)
        BigDecimal descontoIR = rendimento.getValorRendido()
                .multiply(rendimento.getIrAtual())
                .divide(new BigDecimal("100"), RoundingMode.HALF_UP);
        BigDecimal descontoIOF = rendimento.getValorRendido()
                .multiply(rendimento.getIofAtual())
                .divide(new BigDecimal("100"), RoundingMode.HALF_UP);

        BigDecimal valorLiquido = rendimento.getValorRendido()
                .subtract(descontoIR)
                .subtract(descontoIOF);
        rendimento.setValorLiquido(valorLiquido);

        return rendimento;
    }

    private BigDecimal gerarBigDecimal(long minInclusive, long maxExclusive, Random random) {
        return gerarBigDecimal(minInclusive, maxExclusive, random, 2);
    }

    private BigDecimal gerarBigDecimal(long minInclusive, long maxExclusive, Random random, int scale) {
        long valor = minInclusive + random.nextLong(maxExclusive - minInclusive);
        return new BigDecimal(valor).setScale(scale, RoundingMode.HALF_UP);
    }

    private String gerarCPFFake(int numero) {
        // Gera CPFs fake nao validados, apenas para teste
        String base = String.format("%05d", numero % 100000);
        return String.format("1%s%s%s%s%s%s%s%s%s%s",
                base.charAt(0), base.charAt(1), base.charAt(2), base.charAt(3),
                base.charAt(4), (numero / 10) % 10, (numero / 100) % 10, (numero / 1000) % 10,
                (numero / 10000) % 10, (numero / 100000) % 10
        );
    }
}

