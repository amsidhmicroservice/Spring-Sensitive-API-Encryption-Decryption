package com.amsidh.mvc.filter;

import com.amsidh.mvc.util.HybridEncryptionDecryptionUtil;
import com.amsidh.mvc.wrapper.EncryptResponseWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class EncryptResponseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialize filter if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        EncryptResponseWrapper responseWrapper = new EncryptResponseWrapper(httpResponse);
        chain.doFilter(request, responseWrapper);

        String contentType = httpRequest.getContentType();
        String responseBody = responseWrapper.getCaptureAsString();
        String encryptedResponseBody;

        if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            encryptedResponseBody = "{\"data\":\"" + HybridEncryptionDecryptionUtil.encrypt(responseBody) + "\"}";
        } else if (contentType != null && contentType.contains(MediaType.APPLICATION_XML_VALUE)) {
            encryptedResponseBody = "<data>" + HybridEncryptionDecryptionUtil.encrypt(responseBody) + "</data>";
        } else {
            encryptedResponseBody = responseBody;
        }

        byte[] encryptedResponseBytes = encryptedResponseBody.getBytes(StandardCharsets.UTF_8);
        httpResponse.setContentLength(encryptedResponseBytes.length);
        httpResponse.getOutputStream().write(encryptedResponseBytes);
    }

    @Override
    public void destroy() {
        // Cleanup filter if needed
    }
}
