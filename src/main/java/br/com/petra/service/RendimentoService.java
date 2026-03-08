package br.com.petra.service;

import br.com.petra.domain.RendimentoDiario;
import br.com.petra.repository.InvestimentoRepository;
import br.com.petra.repository.RendimentoDiarioRepository;
import br.com.petra.service.dto.RendimentoRequestDTO;
import br.com.petra.service.dto.RendimentoResponseDTO;
import br.com.petra.service.mapper.RendimentoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RendimentoService {

    private final RendimentoDiarioRepository rendimentoDiarioRepository;
    private final InvestimentoRepository investimentoRepository;
    private final RendimentoMapper rendimentoMapper;

    public RendimentoResponseDTO create(UUID investimentoId, RendimentoRequestDTO dto) {
        RendimentoDiario entity = rendimentoMapper.toEntity(dto);
        entity.setInvestimento(investimentoRepository.findById(investimentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Investimento invalido")));
        return rendimentoMapper.toDto(rendimentoDiarioRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public RendimentoResponseDTO findById(UUID id) {
        return rendimentoMapper.toDto(getEntity(id));
    }

    @Transactional(readOnly = true)
    public Page<RendimentoResponseDTO> findAll(Pageable pageable) {
        return rendimentoDiarioRepository.findAll(pageable).map(rendimentoMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<RendimentoResponseDTO> findByInvestimento(UUID investimentoId, Pageable pageable) {
        if (!investimentoRepository.existsById(investimentoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento nao encontrado");
        }
        return rendimentoDiarioRepository.findByInvestimentoId(investimentoId, pageable).map(rendimentoMapper::toDto);
    }

    public RendimentoResponseDTO update(UUID id, RendimentoRequestDTO dto) {
        RendimentoDiario entity = getEntity(id);
        rendimentoMapper.updateEntityFromDto(dto, entity);
        return rendimentoMapper.toDto(rendimentoDiarioRepository.save(entity));
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

