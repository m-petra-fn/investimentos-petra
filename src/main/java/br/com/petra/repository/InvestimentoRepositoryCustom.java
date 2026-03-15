package br.com.petra.repository;

import br.com.petra.domain.Investimento;

import java.util.Optional;
import java.util.UUID;

public interface InvestimentoRepositoryCustom {

    Optional<Investimento> findByIdQueryDsl(UUID id);
}

