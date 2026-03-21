package br.com.petra.web.rest;

import br.com.petra.service.InvestimentoService;
import br.com.petra.service.dto.InvestimentoRequestDTO;
import br.com.petra.service.dto.InvestimentoResponseDTO;
import br.com.petra.service.dto.ResponseJsonDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/investimentos")
@RequiredArgsConstructor
@Slf4j
public class InvestimentoResource {

    private final InvestimentoService investimentoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseJsonDTO<InvestimentoResponseDTO> create(
            @Valid @RequestBody InvestimentoRequestDTO dto
    ) {
        log.info("Recebida requisicao para criar investimento");
        return investimentoService.create(dto);
    }

    @GetMapping("/{id}")
    public ResponseJsonDTO<InvestimentoResponseDTO> findById(
            @PathVariable UUID id
    ) {
        log.info("Recebida requisicao para buscar investimento por id: {}", id);
        return investimentoService.findById(id);
    }

    @GetMapping
    public ResponseJsonDTO<List<InvestimentoResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Recebida requisicao para listar investimentos. page={}, size={}", page, size);
        return investimentoService.findAll(PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    public ResponseJsonDTO<InvestimentoResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody InvestimentoRequestDTO dto
    ) {
        log.info("Recebida requisicao para atualizar investimento com id: {}", id);
        return investimentoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id
    ) {
        log.info("Recebida requisicao para remover investimento com id: {}", id);
        investimentoService.delete(id);
    }
}
