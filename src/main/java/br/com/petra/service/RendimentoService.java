package br.com.petra.service;

import br.com.petra.domain.RendimentoDiario;
import br.com.petra.repository.InvestimentoRepository;
import br.com.petra.repository.RendimentoDiarioRepository;
import br.com.petra.service.dto.RendimentoRequestDTO;
import br.com.petra.service.dto.RendimentoResponseDTO;
import br.com.petra.service.dto.ResponseJsonDTO;
import br.com.petra.service.mapper.RendimentoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class RendimentoService {

    private final RendimentoDiarioRepository rendimentoDiarioRepository;
    private final InvestimentoRepository investimentoRepository;
    private final RendimentoMapper rendimentoMapper;

    public ResponseJsonDTO<RendimentoResponseDTO> create(UUID investimentoId, RendimentoRequestDTO dto) {
        log.info("Iniciando criacao de rendimento para investimentoId={}", investimentoId);
        RendimentoDiario entity = rendimentoMapper.toEntity(dto);
        entity.setInvestimento(investimentoRepository.findById(investimentoId)
                .orElseThrow(() -> {
                    log.warn("Investimento invalido para criacao de rendimento. investimentoId={}", investimentoId);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Investimento invalido");
                }));
        return ResponseJsonDTO.single(rendimentoMapper.toDto(rendimentoDiarioRepository.save(entity)));
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<RendimentoResponseDTO> findById(UUID id) {
        log.info("Buscando rendimento por id={}", id);
        return ResponseJsonDTO.single(rendimentoMapper.toDto(getEntity(id)));
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<List<RendimentoResponseDTO>> findAll(Pageable pageable) {
        log.info("Listando rendimentos. pageNumber={}, pageSize={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<RendimentoResponseDTO> page = rendimentoDiarioRepository.findAll(pageable).map(rendimentoMapper::toDto);
        return ResponseJsonDTO.paged(page);
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
        Page<RendimentoResponseDTO> page = rendimentoDiarioRepository.findByInvestimentoId(investimentoId, pageable)
                .map(rendimentoMapper::toDto);
        return ResponseJsonDTO.paged(page);
    }

    public ResponseJsonDTO<RendimentoResponseDTO> update(UUID id, RendimentoRequestDTO dto) {
        log.info("Atualizando rendimento id={}", id);
        RendimentoDiario entity = getEntity(id);
        rendimentoMapper.updateEntityFromDto(dto, entity);
        return ResponseJsonDTO.single(rendimentoMapper.toDto(rendimentoDiarioRepository.save(entity)));
    }

    public void delete(UUID id) {
        log.info("Removendo rendimento id={}", id);
        if (!rendimentoDiarioRepository.existsById(id)) {
            log.warn("Tentativa de remover rendimento inexistente id={}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rendimento nao encontrado");
        }
        rendimentoDiarioRepository.deleteById(id);
    }

    private RendimentoDiario getEntity(UUID id) {
        return rendimentoDiarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rendimento nao encontrado para id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Rendimento nao encontrado");
                });
    }
}

