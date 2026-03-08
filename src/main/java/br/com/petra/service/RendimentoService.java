package br.com.petra.service;

import br.com.petra.domain.RendimentoDiario;
import br.com.petra.repository.InvestimentoRepository;
import br.com.petra.repository.RendimentoDiarioRepository;
import br.com.petra.service.dto.RendimentoRequestDTO;
import br.com.petra.service.dto.RendimentoResponseDTO;
import br.com.petra.service.dto.ResponseJsonDTO;
import br.com.petra.service.mapper.RendimentoMapper;
import lombok.RequiredArgsConstructor;
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
public class RendimentoService {

    private final RendimentoDiarioRepository rendimentoDiarioRepository;
    private final InvestimentoRepository investimentoRepository;
    private final RendimentoMapper rendimentoMapper;

    public ResponseJsonDTO<RendimentoResponseDTO> create(UUID investimentoId, RendimentoRequestDTO dto) {
        RendimentoDiario entity = rendimentoMapper.toEntity(dto);
        entity.setInvestimento(investimentoRepository.findById(investimentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Investimento invalido")));
        return ResponseJsonDTO.single(rendimentoMapper.toDto(rendimentoDiarioRepository.save(entity)));
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<RendimentoResponseDTO> findById(UUID id) {
        return ResponseJsonDTO.single(rendimentoMapper.toDto(getEntity(id)));
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<List<RendimentoResponseDTO>> findAll(Pageable pageable) {
        Page<RendimentoResponseDTO> page = rendimentoDiarioRepository.findAll(pageable).map(rendimentoMapper::toDto);
        return ResponseJsonDTO.paged(page);
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<List<RendimentoResponseDTO>> findByInvestimento(UUID investimentoId, Pageable pageable) {
        if (!investimentoRepository.existsById(investimentoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento nao encontrado");
        }
        Page<RendimentoResponseDTO> page = rendimentoDiarioRepository.findByInvestimentoId(investimentoId, pageable)
                .map(rendimentoMapper::toDto);
        return ResponseJsonDTO.paged(page);
    }

    public ResponseJsonDTO<RendimentoResponseDTO> update(UUID id, RendimentoRequestDTO dto) {
        RendimentoDiario entity = getEntity(id);
        rendimentoMapper.updateEntityFromDto(dto, entity);
        return ResponseJsonDTO.single(rendimentoMapper.toDto(rendimentoDiarioRepository.save(entity)));
    }

    public void delete(UUID id) {
        if (!rendimentoDiarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rendimento nao encontrado");
        }
        rendimentoDiarioRepository.deleteById(id);
    }

    private RendimentoDiario getEntity(UUID id) {
        return rendimentoDiarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rendimento nao encontrado"));
    }
}

