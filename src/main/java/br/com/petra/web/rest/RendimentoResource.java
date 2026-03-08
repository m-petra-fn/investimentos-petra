package br.com.petra.web.rest;

import br.com.petra.service.RendimentoService;
import br.com.petra.service.dto.RendimentoRequestDTO;
import br.com.petra.service.dto.RendimentoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RendimentoResource {

    private final RendimentoService rendimentoService;

    @PostMapping("/investimentos/{investimentoId}/rendimentos")
    @ResponseStatus(HttpStatus.CREATED)
    public RendimentoResponseDTO create(
            @PathVariable UUID investimentoId,
            @Valid @RequestBody RendimentoRequestDTO dto
    ) {
        return rendimentoService.create(investimentoId, dto);
    }

    @GetMapping("/rendimentos/{id}")
    public RendimentoResponseDTO findById(@PathVariable UUID id) {
        return rendimentoService.findById(id);
    }

    @GetMapping("/rendimentos")
    public Page<RendimentoResponseDTO> findAll(Pageable pageable) {
        return rendimentoService.findAll(pageable);
    }

    @GetMapping("/investimentos/{investimentoId}/rendimentos")
    public Page<RendimentoResponseDTO> findByInvestimento(@PathVariable UUID investimentoId, Pageable pageable) {
        return rendimentoService.findByInvestimento(investimentoId, pageable);
    }

    @PutMapping("/rendimentos/{id}")
    public RendimentoResponseDTO update(@PathVariable UUID id, @Valid @RequestBody RendimentoRequestDTO dto) {
        return rendimentoService.update(id, dto);
    }

    @DeleteMapping("/rendimentos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        rendimentoService.delete(id);
    }
}

