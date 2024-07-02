package com.amsidh.mvc.wrapper;

import com.amsidh.mvc.util.EncryptDecryptUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class DecryptedRequestWrapper extends HttpServletRequestWrapper {
    private final String decryptedRequestBody;

    public DecryptedRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        String contentType = request.getContentType();
        String body = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        if (contentType != null && contentType.contains("application/json")) {
            // Handle JSON content
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(body);
            String decryptedValue = decrypt(rootNode.get("data").asText());
            this.decryptedRequestBody = decryptedValue;
        } else if (contentType != null && contentType.contains("application/xml")) {
            // Handle XML content
            String decryptedValue = decryptXml(body);
            this.decryptedRequestBody = decryptedValue;
        } else {
            // Default to plain text or other types if needed
            this.decryptedRequestBody = body;
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(decryptedRequestBody.getBytes()), StandardCharsets.UTF_8));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private final ByteArrayInputStream inputStream = new ByteArrayInputStream(decryptedRequestBody.getBytes());

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // No implementation needed
            }
        };
    }

    private String decrypt(String encryptedData) {
        return EncryptDecryptUtil.decrypt(encryptedData);
    }

    private String decryptXml(String xml) throws IOException {
        // Simplified XML parsing for extracting and decrypting the <data> element content
        try {
            int start = xml.indexOf("<data>") + 6;
            int end = xml.indexOf("</data>");
            String encryptedData = xml.substring(start, end);
            return decrypt(encryptedData);
        } catch (Exception e) {
            throw new IOException("Failed to parse and decrypt XML", e);
        }
    }
}
