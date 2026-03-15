package br.com.petra.repository;

import br.com.petra.domain.RendimentoDiario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RendimentoDiarioRepository extends JpaRepository<RendimentoDiario, UUID>, RendimentoDiarioRepositoryCustom {

    Page<RendimentoDiario> findByInvestimentoId(UUID investimentoId, Pageable pageable);
}

