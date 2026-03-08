package br.com.petra.repository;

import br.com.petra.domain.TipoInvestimentoDominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoInvestimentoDominioRepository extends JpaRepository<TipoInvestimentoDominio, Long> {

    Optional<TipoInvestimentoDominio> findByCodigo(String codigo);
}

