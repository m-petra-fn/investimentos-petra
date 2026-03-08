package br.com.petra.repository;

import br.com.petra.domain.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvestimentoRepository extends JpaRepository<Investimento, UUID> {
}

