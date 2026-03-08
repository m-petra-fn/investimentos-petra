package br.com.petra.service.dto;

public record ResponseJsonDTO<T>(
        T data,
        MetaDTO meta
) {
}

