package br.com.petra.service.dto;

import java.time.LocalDateTime;

public record MetaDTO(
        LocalDateTime requestDateTime,
        int page,
        int size,
        long totalRecords,
        int totalPages
) {
}
