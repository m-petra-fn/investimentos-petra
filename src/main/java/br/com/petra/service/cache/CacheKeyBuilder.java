package br.com.petra.service.cache;

import br.com.petra.config.cache.CacheRedisProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CacheKeyBuilder {

    private final String rootPrefix;

    public CacheKeyBuilder(CacheRedisProperties properties) {
        this.rootPrefix = properties.getKeyPrefix();
    }

    public String investimentoById(UUID id) {
        return rootPrefix + ":investimento:id:" + id;
    }

    public String investimentoPage(Pageable pageable) {
        return rootPrefix + ":investimento:all:" + pageDescriptor(pageable);
    }

    public String investimentoPagePrefix() {
        return rootPrefix + ":investimento:all:";
    }

    public String rendimentoById(UUID id) {
        return rootPrefix + ":rendimento:id:" + id;
    }

    public String rendimentoPage(Pageable pageable) {
        return rootPrefix + ":rendimento:all:" + pageDescriptor(pageable);
    }

    public String rendimentoPagePrefix() {
        return rootPrefix + ":rendimento:all:";
    }

    public String rendimentoByInvestimento(UUID investimentoId, Pageable pageable) {
        return rootPrefix + ":rendimento:investimento:" + investimentoId + ":" + pageDescriptor(pageable);
    }

    public String rendimentoByInvestimentoPrefix(UUID investimentoId) {
        return rootPrefix + ":rendimento:investimento:" + investimentoId + ":";
    }

    private String pageDescriptor(Pageable pageable) {
        String sort = pageable.getSort().isSorted() ? pageable.getSort().toString().replace(" ", "") : "unsorted";
        return "p" + pageable.getPageNumber() + ":s" + pageable.getPageSize() + ":sort:" + sort;
    }
}

