package br.com.petra.service;

import br.com.petra.domain.Investimento;
import br.com.petra.repository.InvestimentoRepository;
import br.com.petra.repository.MoedaDominioRepository;
import br.com.petra.repository.TipoIndexacaoDominioRepository;
import br.com.petra.repository.TipoInvestimentoDominioRepository;
import br.com.petra.service.dto.InvestimentoRequestDTO;
import br.com.petra.service.dto.InvestimentoResponseDTO;
import br.com.petra.service.dto.ResponseJsonDTO;
import br.com.petra.service.mapper.InvestimentoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final MoedaDominioRepository moedaDominioRepository;
    private final TipoInvestimentoDominioRepository tipoInvestimentoDominioRepository;
    private final TipoIndexacaoDominioRepository tipoIndexacaoDominioRepository;
    private final InvestimentoMapper investimentoMapper;

    @CacheEvict(cacheNames = "investimento-all", allEntries = true)
    public ResponseJsonDTO<InvestimentoResponseDTO> create(InvestimentoRequestDTO dto) {
        log.info("Iniciando criacao de investimento para cpfInvestidor={}", dto.cpfInvestidor());
        Investimento entity = investimentoMapper.toEntity(dto);
        applyDomainReferences(entity, dto);
        InvestimentoResponseDTO response = investimentoMapper.toDto(investimentoRepository.save(entity));
        return ResponseJsonDTO.single(response);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "investimento-by-id", key = "#id")
    public ResponseJsonDTO<InvestimentoResponseDTO> findById(UUID id) {
        log.info("Buscando investimento por id={}", id);
        return ResponseJsonDTO.single(investimentoMapper.toDto(getEntity(id)));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "investimento-all", key = "#pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort")
    public ResponseJsonDTO<List<InvestimentoResponseDTO>> findAll(Pageable pageable) {
        log.info("Listando investimentos. pageNumber={}, pageSize={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<InvestimentoResponseDTO> page = investimentoRepository.findAll(pageable).map(investimentoMapper::toDto);
        return ResponseJsonDTO.paged(page);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "investimento-by-id", key = "#id"),
            @CacheEvict(cacheNames = "investimento-all", allEntries = true)
    })
    public ResponseJsonDTO<InvestimentoResponseDTO> update(UUID id, InvestimentoRequestDTO dto) {
        log.info("Atualizando investimento id={} para cpfInvestidor={}", id, dto.cpfInvestidor());
        Investimento entity = getEntity(id);
        investimentoMapper.updateEntityFromDto(dto, entity);
        applyDomainReferences(entity, dto);
        InvestimentoResponseDTO response = investimentoMapper.toDto(investimentoRepository.save(entity));
        return ResponseJsonDTO.single(response);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "investimento-by-id", key = "#id"),
            @CacheEvict(cacheNames = "investimento-all", allEntries = true)
    })
    public void delete(UUID id) {
        log.info("Removendo investimento id={}", id);
        if (!investimentoRepository.existsById(id)) {
            log.warn("Tentativa de remover investimento inexistente id={}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento nao encontrado");
        }
        investimentoRepository.deleteById(id);
    }

    private Investimento getEntity(UUID id) {
        return investimentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Investimento nao encontrado para id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento nao encontrado");
                });
    }

    private void applyDomainReferences(Investimento entity, InvestimentoRequestDTO dto) {
        entity.setMoeda(
                moedaDominioRepository.findById(dto.moedaId())
                        .orElseThrow(() -> {
                            log.warn("Moeda invalida para moedaId={}", dto.moedaId());
                            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Moeda invalida");
                        })
        );
        entity.setTipoInvestimento(
                tipoInvestimentoDominioRepository.findById(dto.tipoInvestimentoId())
                        .orElseThrow(() -> {
                            log.warn("Tipo de investimento invalido para tipoInvestimentoId={}", dto.tipoInvestimentoId());
                            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de investimento invalido");
                        })
        );
        entity.setTipoIndexacao(
                tipoIndexacaoDominioRepository.findById(dto.tipoIndexacaoId())
                        .orElseThrow(() -> {
                            log.warn("Tipo de indexacao invalido para tipoIndexacaoId={}", dto.tipoIndexacaoId());
                            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de indexacao invalido");
                        })
        );
    }
}

