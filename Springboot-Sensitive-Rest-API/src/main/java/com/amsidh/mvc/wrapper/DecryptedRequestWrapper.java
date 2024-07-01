package com.amsidh.mvc.wrapper;

import com.amsidh.mvc.util.EncryptDecryptUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class DecryptedRequestWrapper extends HttpServletRequestWrapper {
    private final String decryptedRequestBody;
    private final Set<String> decryptedAttributes = Set.of("username");

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
            decryptJsonNode(rootNode);
            this.decryptedRequestBody = objectMapper.writeValueAsString(rootNode);
        } else if (contentType != null && contentType.contains("application/xml")) {
            // Handle XML content
            this.decryptedRequestBody = decryptXml(body);
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

    private void decryptJsonNode(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (shouldDecrypt(field.getKey())) {
                    log.info("field {}, value {} before decrypting", field.getKey(), field.getValue().asText());
                    String decryptedValue = decrypt(field.getValue().asText());
                    log.info("field {}, value {} after decrypted", field.getKey(), decryptedValue);
                    objectNode.put(field.getKey(), decryptedValue);
                } else {
                    decryptJsonNode(field.getValue()); // Recur for nested objects/arrays
                }
            }
        } else if (node.isArray()) {
            for (JsonNode arrayItem : node) {
                decryptJsonNode(arrayItem); // Recur for nested objects/arrays
            }
        }
    }

    private String decryptXml(String xml) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            decryptXmlNode(document.getDocumentElement());

            StringWriter writer = new StringWriter();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            throw new IOException("Failed to parse and decrypt XML", e);
        }
    }

    private void decryptXmlNode(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (shouldDecrypt(element.getTagName())) {
                String decryptedValue = decrypt(element.getTextContent());
                element.setTextContent(decryptedValue);
            }

            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                decryptXmlNode(childNodes.item(i)); // Recur for nested elements
            }
        }
    }

    private boolean shouldDecrypt(String key) {
        return decryptedAttributes.contains(key) || key.startsWith("encrypt");
    }

    private String decrypt(String encryptedData) {
        return EncryptDecryptUtil.decrypt(encryptedData);
    }
}
