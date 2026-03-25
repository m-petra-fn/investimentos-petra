package br.com.petra.web.rest;

import br.com.petra.service.RendimentoService;
import br.com.petra.service.dto.RendimentoRequestDTO;
import br.com.petra.service.dto.RendimentoResponseDTO;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RendimentoResource {

    private final RendimentoService rendimentoService;

    @PostMapping("/investimentos/{investimentoId}/rendimentos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseJsonDTO<RendimentoResponseDTO> create(
            @PathVariable UUID investimentoId,
            @Valid @RequestBody RendimentoRequestDTO dto
    ) {
        log.info("Recebida requisicao para criar rendimento para investimentoId: {}", investimentoId);
        return rendimentoService.create(investimentoId, dto);
    }

    @GetMapping("/rendimentos/{id}")
    public ResponseJsonDTO<RendimentoResponseDTO> findById(@PathVariable UUID id) {
        log.info("Recebida requisicao para buscar rendimento por id: {}", id);
        return rendimentoService.findById(id);
    }

    @GetMapping("/rendimentos")
    public ResponseJsonDTO<List<RendimentoResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Recebida requisicao para listar rendimentos. page={}, size={}", page, size);
        return rendimentoService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/investimentos/{investimentoId}/rendimentos")
    public ResponseJsonDTO<List<RendimentoResponseDTO>> findByInvestimento(
            @PathVariable UUID investimentoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info(
                "Recebida requisicao para listar rendimentos por investimentoId: {}. page={}, size={}",
                investimentoId,
                page,
                size
        );
        return rendimentoService.findByInvestimento(investimentoId, PageRequest.of(page, size));
    }

    @PutMapping("/rendimentos/{id}")
    public ResponseJsonDTO<RendimentoResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody RendimentoRequestDTO dto) {
        log.info("Recebida requisicao para atualizar rendimento com id: {}", id);
        return rendimentoService.update(id, dto);
    }

    @DeleteMapping("/rendimentos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        log.info("Recebida requisicao para remover rendimento com id: {}", id);
        rendimentoService.delete(id);
    }
}
