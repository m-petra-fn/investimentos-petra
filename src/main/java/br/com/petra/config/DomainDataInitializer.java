package br.com.petra.config;

import br.com.petra.domain.MoedaDominio;
import br.com.petra.domain.TipoIndexacaoDominio;
import br.com.petra.domain.TipoInvestimentoDominio;
import br.com.petra.repository.MoedaDominioRepository;
import br.com.petra.repository.TipoIndexacaoDominioRepository;
import br.com.petra.repository.TipoInvestimentoDominioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DomainDataInitializer implements CommandLineRunner {

    private final MoedaDominioRepository moedaDominioRepository;
    private final TipoInvestimentoDominioRepository tipoInvestimentoDominioRepository;
    private final TipoIndexacaoDominioRepository tipoIndexacaoDominioRepository;

    @Override
    public void run(String... args) {
        createMoeda("BRL", "Real Brasileiro");

        createTipoInvestimento("CDB", "Certificado de Deposito Bancario");
        createTipoInvestimento("LCI", "Letra de Credito Imobiliario");
        createTipoInvestimento("LCA", "Letra de Credito do Agronegocio");
        createTipoInvestimento("TESOURO", "Tesouro Direto");

        createTipoIndexacao("PREFIXADO", "Taxa definida no momento da aplicacao");
        createTipoIndexacao("POSFIXADO", "Taxa atrelada a um indice");
    }

    private void createMoeda(String codigo, String descricao) {
        moedaDominioRepository.findByCodigo(codigo).orElseGet(() -> {
            MoedaDominio moeda = new MoedaDominio();
            moeda.setCodigo(codigo);
            moeda.setDescricao(descricao);
            return moedaDominioRepository.save(moeda);
        });
    }

    private void createTipoInvestimento(String codigo, String descricao) {
        tipoInvestimentoDominioRepository.findByCodigo(codigo).orElseGet(() -> {
            TipoInvestimentoDominio tipo = new TipoInvestimentoDominio();
            tipo.setCodigo(codigo);
            tipo.setDescricao(descricao);
            return tipoInvestimentoDominioRepository.save(tipo);
        });
    }

    private void createTipoIndexacao(String codigo, String descricao) {
        tipoIndexacaoDominioRepository.findByCodigo(codigo).orElseGet(() -> {
            TipoIndexacaoDominio tipo = new TipoIndexacaoDominio();
            tipo.setCodigo(codigo);
            tipo.setDescricao(descricao);
            return tipoIndexacaoDominioRepository.save(tipo);
        });
    }
}

