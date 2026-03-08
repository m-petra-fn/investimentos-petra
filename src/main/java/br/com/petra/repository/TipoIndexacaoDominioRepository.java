package br.com.petra.repository;

import br.com.petra.domain.TipoIndexacaoDominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoIndexacaoDominioRepository extends JpaRepository<TipoIndexacaoDominio, Long> {

    Optional<TipoIndexacaoDominio> findByCodigo(String codigo);
}

