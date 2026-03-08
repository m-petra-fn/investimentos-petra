package br.com.petra.web.rest;

import br.com.petra.service.InvestimentoService;
import br.com.petra.service.dto.InvestimentoRequestDTO;
import br.com.petra.service.dto.InvestimentoResponseDTO;
import br.com.petra.service.dto.ResponseJsonDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/investimentos")
@RequiredArgsConstructor
public class InvestimentoResource {

    private final InvestimentoService investimentoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseJsonDTO<InvestimentoResponseDTO> create(@Valid @RequestBody InvestimentoRequestDTO dto) {
        return investimentoService.create(dto);
    }

    @GetMapping("/{id}")
    public ResponseJsonDTO<InvestimentoResponseDTO> findById(@PathVariable UUID id) {
        return investimentoService.findById(id);
    }

    @GetMapping
    public ResponseJsonDTO<List<InvestimentoResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return investimentoService.findAll(PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    public ResponseJsonDTO<InvestimentoResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody InvestimentoRequestDTO dto) {
        return investimentoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        investimentoService.delete(id);
    }
}
