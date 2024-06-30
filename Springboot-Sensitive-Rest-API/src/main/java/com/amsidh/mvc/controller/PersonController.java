package com.amsidh.mvc.controller;

import com.amsidh.mvc.model.PersonRequest;
import com.amsidh.mvc.model.PersonResponse;
import com.amsidh.mvc.util.EncryptDecryptUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/persons")
@Validated
@Slf4j
public class PersonController {

    @PostMapping
    public PersonResponse savePerson(@RequestBody @Valid PersonRequest personRequest) {
        log.info("Inside savePerson method of class PersonController");
        final PersonResponse personResponse = PersonResponse.builder().username(personRequest.getUsername()).message(String
                        .format("Person with username %s is saved successfully", personRequest.getUsername()))
                .build();
        try {
            personResponse.encryptSensitiveData(); // Encrypt sensitive data
        } catch (Exception e) {
            log.error("Error during encryption/decryption process. {}", e.getMessage(), e);
            throw new RuntimeException("Encryption/Decryption error", e);
        }
        return personResponse;
    }

    @GetMapping("/{username}")
    public PersonResponse getPersonByPersonId(@PathVariable(name = "username") String username) {
        log.info("Inside getPersonByPersonId method of class PersonController");
        username = EncryptDecryptUtil.decrypt(username);
        PersonResponse response = PersonResponse.builder().username(username).message(String
                        .format("Person with username %s is found and returned successfully", username))
                .build();
        try {
            response.encryptSensitiveData(); // Encrypt sensitive data
        } catch (Exception e) {
            log.error("Error during encryption/decryption process. {}", e.getMessage(), e);
            throw new RuntimeException("Encryption/Decryption error", e);
        }
        return response;
    }
}
