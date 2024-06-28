package com.amsidh.mvc;

import com.amsidh.mvc.model.PersonRequest;
import com.amsidh.mvc.model.PersonResponse;
import com.amsidh.mvc.util.RSAUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;

@RequiredArgsConstructor
@SpringBootApplication
@Slf4j
public class SpringBootSensitiveClientApplication implements CommandLineRunner {

    private final RestTemplate restTemplate;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSensitiveClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Load public and private keys
        PublicKey publicKey = RSAUtil.getPublicKey("C:/Users/amsid/Documents/IntellijProjects/Spring-Sensitive-API-Encryption-Decryption/SpringBoot-Sensitive-Client/src/main/resources/rsa/publicKey.pem"); // Replace with your public key path
        PrivateKey privateKey = RSAUtil.getPrivateKey("C:/Users/amsid/Documents/IntellijProjects/Spring-Sensitive-API-Encryption-Decryption/SpringBoot-Sensitive-Client/src/main/resources/rsa/privateKey.pem"); // Replace with your private key path

        // Create a PersonRequest object
        PersonRequest personRequest = PersonRequest.builder()
                .username("john_doe")
                .emailId("john@example.com")
                .password("password123")
                .build();

        // Encrypt sensitive data in the request
        personRequest.encryptSensitiveData(publicKey);

        // Convert the request object to JSON and encode in Base64
        String requestJson = new ObjectMapper().writeValueAsString(personRequest);
        String encryptedRequest = Base64.getEncoder().encodeToString(requestJson.getBytes());

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(encryptedRequest, headers);

        // Send POST request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "http://localhost:8181/persons", requestEntity, String.class);

        // Decrypt the response
        byte[] responseBytes = Base64.getDecoder().decode(responseEntity.getBody());
        String decryptedResponse = RSAUtil.decrypt(responseBytes, privateKey);

        // Convert JSON response to PersonResponse object
        PersonResponse personResponse = new ObjectMapper().readValue(decryptedResponse, PersonResponse.class);

        // Print the response
        System.out.println("Response: " + personResponse);

    }
}
