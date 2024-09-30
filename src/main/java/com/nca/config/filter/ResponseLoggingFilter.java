package com.nca.config.filter;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@code ResponseLoggingFilter} for filtering all requests and logging it.
 */

@Slf4j
@Component
public class ResponseLoggingFilter extends OncePerRequestFilter {

    /**
     * {@code doFilter} filtering all requests and logging request and response data.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws IOException, ServletException {

        // Log response information
        log.debug("Response status {} for processed request {} {}",
                response.getStatus(),
                request.getMethod(),
                request.getRequestURL());
    }
}
