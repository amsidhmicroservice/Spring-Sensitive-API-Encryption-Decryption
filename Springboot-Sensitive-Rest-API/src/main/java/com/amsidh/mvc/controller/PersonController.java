package com.amsidh.mvc.controller;

import com.amsidh.mvc.model.PersonRequest;
import com.amsidh.mvc.model.PersonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/persons")
@Validated
@Slf4j
public class PersonController {


    @PostMapping
    public PersonResponse savePerson(@RequestBody @Valid PersonRequest personRequest) {
        try {
            log.info("Inside savePerson method of class PersonController. Saving Person with details {}", personRequest.toString());
            // Create response
            PersonResponse personResponse = PersonResponse.builder()
                    .username(personRequest.getUsername())
                    .message(String.format("Person with username %s is saved successfully", personRequest.getUsername()))
                    .build();
            // Encrypt sensitive data in the response
            personResponse.encryptSensitiveData();
            return personResponse;
        } catch (Exception e) {
            log.error("Error during encryption/decryption process. {}", e.getMessage(), e);
            throw new RuntimeException("Encryption/Decryption error", e);
        }
    }
}
