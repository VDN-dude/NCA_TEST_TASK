package com.nca.config.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@code RequestLoggingFilter} for filtering all requests and logging it.
 */

@Slf4j
@WebFilter("/*")
public class RequestLoggingFilter implements Filter {

    /**
     * {@code doFilter} filtering all requests and logging request and response data.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Log request information
        log.debug("Request processing {} for url {}, by principal {}",
                httpRequest.getMethod(),
                httpRequest.getRequestURL(),
                httpRequest.getUserPrincipal());

        // Proceed with the request
        chain.doFilter(request, response);

        // Log response information
        log.debug("Response status {} for processed request {} {}",
                httpResponse.getStatus(),
                httpRequest.getMethod(),
                httpRequest.getRequestURL());
    }
}
