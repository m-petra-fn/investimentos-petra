package br.com.petra.service.dto;

import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseJsonDTO<T>(
        T data,
        MetaDTO meta
) {

    public static <T> ResponseJsonDTO<T> single(T data) {
        return new ResponseJsonDTO<>(
                data,
                new MetaDTO(LocalDateTime.now(), 0, 1, 1, 0)
        );
    }

    public static <T> ResponseJsonDTO<List<T>> paged(Page<T> page) {
        return new ResponseJsonDTO<>(
                page.getContent(),
                new MetaDTO(
                        LocalDateTime.now(),
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages()
                )
        );
    }
}
