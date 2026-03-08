package br.com.petra.web.rest;

import br.com.petra.service.RendimentoService;
import br.com.petra.service.dto.RendimentoRequestDTO;
import br.com.petra.service.dto.RendimentoResponseDTO;
import br.com.petra.service.dto.ResponseJsonDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RendimentoResource {

    private final RendimentoService rendimentoService;

    @PostMapping("/investimentos/{investimentoId}/rendimentos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseJsonDTO<RendimentoResponseDTO> create(
            @PathVariable UUID investimentoId,
            @Valid @RequestBody RendimentoRequestDTO dto
    ) {
        return rendimentoService.create(investimentoId, dto);
    }

    @GetMapping("/rendimentos/{id}")
    public ResponseJsonDTO<RendimentoResponseDTO> findById(@PathVariable UUID id) {
        return rendimentoService.findById(id);
    }

    @GetMapping("/rendimentos")
    public ResponseJsonDTO<List<RendimentoResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return rendimentoService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/investimentos/{investimentoId}/rendimentos")
    public ResponseJsonDTO<List<RendimentoResponseDTO>> findByInvestimento(
            @PathVariable UUID investimentoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return rendimentoService.findByInvestimento(investimentoId, PageRequest.of(page, size));
    }

    @PutMapping("/rendimentos/{id}")
    public ResponseJsonDTO<RendimentoResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody RendimentoRequestDTO dto) {
        return rendimentoService.update(id, dto);
    }

    @DeleteMapping("/rendimentos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        rendimentoService.delete(id);
    }
}
