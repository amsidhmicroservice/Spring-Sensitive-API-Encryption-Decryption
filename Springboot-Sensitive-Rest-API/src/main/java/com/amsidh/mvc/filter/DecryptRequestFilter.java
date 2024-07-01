package com.amsidh.mvc.filter;

import com.amsidh.mvc.wrapper.DecryptedRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DecryptRequestFilter implements Filter {

    private final Set<String> urlsToDecrypt = Set.of("/persons");


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String requestURI = httpRequest.getRequestURI();
            // Check if the request URI matches the URLs that need decryption
            if (shouldDecryptRequest(requestURI)) {
                chain.doFilter(new DecryptedRequestWrapper(httpRequest), response); // Use injected DecryptedRequestWrapper
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    private boolean shouldDecryptRequest(String requestURI) {
        return urlsToDecrypt.stream().anyMatch(requestURI::startsWith);
    }
}

