package br.com.petra.config.filter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FapiInteractionIdFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "x-fapi-interaction-id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String id = request.getHeader(HEADER_NAME);

        // Coloca no MDC para ficar disponível ao logger

        if (StringUtils.isNotBlank(id)) {
            MDC.put(HEADER_NAME, id);
            try {
                // (Opcional) também escreve no response para facilitar rastreio
                response.setHeader(HEADER_NAME, id);
                filterChain.doFilter(request, response);
            } finally {
                // SEMPRE limpar para evitar vazamento de contexto entre requisições
                MDC.remove(HEADER_NAME);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}