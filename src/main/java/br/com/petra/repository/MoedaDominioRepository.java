package br.com.petra.repository;

import br.com.petra.domain.MoedaDominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoedaDominioRepository extends JpaRepository<MoedaDominio, Long> {

    Optional<MoedaDominio> findByCodigo(String codigo);
}

