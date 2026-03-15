package br.com.petra.repository;

import br.com.petra.domain.RendimentoDiario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface RendimentoDiarioRepositoryCustom {

    Page<RendimentoDiario> findByInvestimentoIdQueryDsl(UUID investimentoId, Pageable pageable);

    Page<RendimentoDiario> findByInvestimentoIdAndPeriodoQueryDsl(
            UUID investimentoId,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    );
}

