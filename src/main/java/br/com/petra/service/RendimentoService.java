package br.com.petra.service;

import br.com.petra.config.cache.CacheRedisProperties;
import br.com.petra.domain.RendimentoDiario;
import br.com.petra.repository.InvestimentoRepository;
import br.com.petra.repository.RendimentoDiarioRepository;
import br.com.petra.service.cache.CacheKeyBuilder;
import br.com.petra.service.cache.CacheStore;
import br.com.petra.service.dto.RendimentoRequestDTO;
import br.com.petra.service.dto.RendimentoResponseDTO;
import br.com.petra.service.dto.ResponseJsonDTO;
import br.com.petra.service.mapper.RendimentoMapper;
import tools.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RendimentoService {

    private static final TypeReference<ResponseJsonDTO<RendimentoResponseDTO>> RENDIMENTO_SINGLE_CACHE_TYPE = new TypeReference<>() {
    };
    private static final TypeReference<ResponseJsonDTO<List<RendimentoResponseDTO>>> RENDIMENTO_PAGE_CACHE_TYPE = new TypeReference<>() {
    };

    private final RendimentoDiarioRepository rendimentoDiarioRepository;
    private final InvestimentoRepository investimentoRepository;
    private final RendimentoMapper rendimentoMapper;
    private final CacheStore cacheStore;
    private final CacheKeyBuilder cacheKeyBuilder;
    private final CacheRedisProperties cacheRedisProperties;

    public ResponseJsonDTO<RendimentoResponseDTO> create(UUID investimentoId, RendimentoRequestDTO dto) {
        log.info("Iniciando criacao de rendimento para investimentoId={}", investimentoId);
        RendimentoDiario entity = rendimentoMapper.toEntity(dto);
        entity.setInvestimento(investimentoRepository.findById(investimentoId)
                .orElseThrow(() -> {
                    log.warn("Investimento invalido para criacao de rendimento. investimentoId={}", investimentoId);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Investimento invalido");
                }));
        ResponseJsonDTO<RendimentoResponseDTO> response = ResponseJsonDTO.single(rendimentoMapper.toDto(rendimentoDiarioRepository.save(entity)));
        invalidateRendimentoCache(response.data().id(), investimentoId);
        return response;
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<RendimentoResponseDTO> findById(UUID id) {
        log.info("Buscando rendimento por id={}", id);
        String key = cacheKeyBuilder.rendimentoById(id);
        Optional<ResponseJsonDTO<RendimentoResponseDTO>> cached = cacheStore.get(key, RENDIMENTO_SINGLE_CACHE_TYPE);
        if (cached.isPresent()) {
            return cached.get();
        }

        ResponseJsonDTO<RendimentoResponseDTO> response = ResponseJsonDTO.single(rendimentoMapper.toDto(getEntity(id)));
        cacheStore.put(key, response, cacheRedisProperties.getTtl());
        return response;
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<List<RendimentoResponseDTO>> findAll(Pageable pageable) {
        log.info("Listando rendimentos. pageNumber={}, pageSize={}", pageable.getPageNumber(), pageable.getPageSize());
        String key = cacheKeyBuilder.rendimentoPage(pageable);
        Optional<ResponseJsonDTO<List<RendimentoResponseDTO>>> cached = cacheStore.get(key, RENDIMENTO_PAGE_CACHE_TYPE);
        if (cached.isPresent()) {
            return cached.get();
        }

        Page<RendimentoResponseDTO> page = rendimentoDiarioRepository.findAll(pageable).map(rendimentoMapper::toDto);
        ResponseJsonDTO<List<RendimentoResponseDTO>> response = ResponseJsonDTO.paged(page);
        cacheStore.put(key, response, cacheRedisProperties.getTtl());
        return response;
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<List<RendimentoResponseDTO>> findByInvestimento(UUID investimentoId, Pageable pageable) {
        log.info(
                "Listando rendimentos por investimentoId={}. pageNumber={}, pageSize={}",
                investimentoId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        if (!investimentoRepository.existsById(investimentoId)) {
            log.warn("Investimento nao encontrado para listar rendimentos. investimentoId={}", investimentoId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento nao encontrado");
        }

        String key = cacheKeyBuilder.rendimentoByInvestimento(investimentoId, pageable);
        Optional<ResponseJsonDTO<List<RendimentoResponseDTO>>> cached = cacheStore.get(key, RENDIMENTO_PAGE_CACHE_TYPE);
        if (cached.isPresent()) {
            return cached.get();
        }

        Page<RendimentoResponseDTO> page = rendimentoDiarioRepository.findByInvestimentoId(investimentoId, pageable)
                .map(rendimentoMapper::toDto);
        ResponseJsonDTO<List<RendimentoResponseDTO>> response = ResponseJsonDTO.paged(page);
        cacheStore.put(key, response, cacheRedisProperties.getTtl());
        return response;
    }

    public ResponseJsonDTO<RendimentoResponseDTO> update(UUID id, RendimentoRequestDTO dto) {
        log.info("Atualizando rendimento id={}", id);
        RendimentoDiario entity = getEntity(id);
        rendimentoMapper.updateEntityFromDto(dto, entity);
        ResponseJsonDTO<RendimentoResponseDTO> response = ResponseJsonDTO.single(rendimentoMapper.toDto(rendimentoDiarioRepository.save(entity)));
        invalidateRendimentoCache(id, entity.getInvestimento().getId());
        return response;
    }

    public void delete(UUID id) {
        log.info("Removendo rendimento id={}", id);
        RendimentoDiario entity = getEntity(id);
        UUID investimentoId = entity.getInvestimento().getId();
        rendimentoDiarioRepository.delete(entity);
        invalidateRendimentoCache(id, investimentoId);
    }

    private void invalidateRendimentoCache(UUID rendimentoId, UUID investimentoId) {
        cacheStore.evict(cacheKeyBuilder.rendimentoById(rendimentoId));
        cacheStore.evictByPrefix(cacheKeyBuilder.rendimentoPagePrefix());
        cacheStore.evictByPrefix(cacheKeyBuilder.rendimentoByInvestimentoPrefix(investimentoId));
    }

    private RendimentoDiario getEntity(UUID id) {
        return rendimentoDiarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rendimento nao encontrado para id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Rendimento nao encontrado");
                });
    }
}

