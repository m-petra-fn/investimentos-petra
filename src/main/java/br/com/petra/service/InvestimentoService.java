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
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final MoedaDominioRepository moedaDominioRepository;
    private final TipoInvestimentoDominioRepository tipoInvestimentoDominioRepository;
    private final TipoIndexacaoDominioRepository tipoIndexacaoDominioRepository;
    private final InvestimentoMapper investimentoMapper;

    public ResponseJsonDTO<InvestimentoResponseDTO> create(InvestimentoRequestDTO dto) {
        Investimento entity = investimentoMapper.toEntity(dto);
        applyDomainReferences(entity, dto);
        InvestimentoResponseDTO response = investimentoMapper.toDto(investimentoRepository.save(entity));
        return ResponseJsonDTO.single(response);
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<InvestimentoResponseDTO> findById(UUID id) {
        return ResponseJsonDTO.single(investimentoMapper.toDto(getEntityByQueryDsl(id)));
    }

    @Transactional(readOnly = true)
    public ResponseJsonDTO<List<InvestimentoResponseDTO>> findAll(Pageable pageable) {
        Page<InvestimentoResponseDTO> page = investimentoRepository.findAll(pageable).map(investimentoMapper::toDto);
        return ResponseJsonDTO.paged(page);
    }

    public ResponseJsonDTO<InvestimentoResponseDTO> update(UUID id, InvestimentoRequestDTO dto) {
        Investimento entity = getEntity(id);
        investimentoMapper.updateEntityFromDto(dto, entity);
        applyDomainReferences(entity, dto);
        InvestimentoResponseDTO response = investimentoMapper.toDto(investimentoRepository.save(entity));
        return ResponseJsonDTO.single(response);
    }

    public void delete(UUID id) {
        if (!investimentoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento nao encontrado");
        }
        investimentoRepository.deleteById(id);
    }

    private Investimento getEntity(UUID id) {
        return investimentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento nao encontrado"));
    }

    private Investimento getEntityByQueryDsl(UUID id) {
        return investimentoRepository.findByIdQueryDsl(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento nao encontrado"));
    }

    private void applyDomainReferences(Investimento entity, InvestimentoRequestDTO dto) {
        entity.setMoeda(
                moedaDominioRepository.findById(dto.moedaId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Moeda invalida"))
        );
        entity.setTipoInvestimento(
                tipoInvestimentoDominioRepository.findById(dto.tipoInvestimentoId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de investimento invalido"))
        );
        entity.setTipoIndexacao(
                tipoIndexacaoDominioRepository.findById(dto.tipoIndexacaoId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de indexacao invalido"))
        );
    }
}

