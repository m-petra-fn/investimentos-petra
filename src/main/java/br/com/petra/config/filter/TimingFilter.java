package br.com.petra.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Objects;

@Component
public class TimingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);
        long startTime = System.currentTimeMillis();

        chain.doFilter(request, wrappedResponse);

        long responseTime = System.currentTimeMillis() - startTime;
        wrappedResponse.addHeader("X-Response-Time-Ms", Objects.toString(responseTime));
        wrappedResponse.copyBodyToResponse();
    }
}
