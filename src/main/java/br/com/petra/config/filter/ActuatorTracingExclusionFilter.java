package br.com.petra.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que exclui requisições ao /actuator/** do tracing para evitar poluição do Zipkin.
 * Este filtro corre antes do TracingPropagationFilter (ordem menor).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ActuatorTracingExclusionFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestPath = request.getRequestURI();
        return requestPath == null || (!requestPath.contains("actuator") && !requestPath.contains("swagger"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}

