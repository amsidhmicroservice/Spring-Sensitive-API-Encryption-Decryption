package com.amsidh.mvc.filter;

import com.amsidh.mvc.wrapper.DecryptRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class DecryptRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest httpServletRequest) {
            String requestURI = httpServletRequest.getRequestURI();
            // Check if the request URI matches the URIs that need decryption
            log.info("Decrypting the request with requestURI {}", requestURI);
            filterChain.doFilter(new DecryptRequestWrapper(httpServletRequest), servletResponse);
        }
    }

}