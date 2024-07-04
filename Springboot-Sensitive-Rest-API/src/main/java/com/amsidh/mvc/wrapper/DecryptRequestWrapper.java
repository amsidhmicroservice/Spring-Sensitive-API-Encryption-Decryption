package com.amsidh.mvc.wrapper;

import com.amsidh.mvc.util.HybridEncryptionDecryptionUtil;
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
public class DecryptRequestWrapper extends HttpServletRequestWrapper {
    private final String decryptedRequestBody;

    public DecryptRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String contentType = request.getContentType();
        String body = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
        if (contentType != null && contentType.contains("application/json")) {
            //Handle JSON Request
            this.decryptedRequestBody = decryptJson(body);
        } else if (contentType != null && contentType.contains("application/xml")) {
            //Handle XML Request
            this.decryptedRequestBody = decryptXml(body);
        } else {
            //Handle Other Request
            this.decryptedRequestBody = body;
        }

    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(decryptedRequestBody.getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private final ByteArrayInputStream inputStream = new ByteArrayInputStream(decryptedRequestBody.getBytes(StandardCharsets.UTF_8));

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

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }

    private String decryptJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);
            return decrypt(rootNode.get("data").asText());
        } catch (IOException ioException) {
            log.error("IOException occurred in decryptJson method of DecryptRequestWrapper class. Exception message is {}", ioException.getMessage(), ioException);
            throw new RuntimeException("Unable to decrypt json request");
        }
    }

    private String decryptXml(String xml) {
        try {
            int start = xml.indexOf("<data>" + 6);
            int end = xml.indexOf("</data>");
            String encryptedData = xml.substring(start, end);
            return decrypt(encryptedData);
        } catch (Exception exception) {
            log.error("JsonProcessingException occurred in decryptJson method of DecryptRequestWrapper class. Exception message is {}", exception.getMessage(), exception);
            throw new RuntimeException("Unable to decrypt xml request");
        }
    }

    private String decrypt(String encryptedData) {
        return HybridEncryptionDecryptionUtil.decrypt(encryptedData);
    }

}