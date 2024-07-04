package com.amsidh.mvc.controller;

import com.amsidh.mvc.model.PersonRequest;
import com.amsidh.mvc.model.PersonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/persons")
@Validated
@Slf4j
public class PersonController {

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonResponse savePerson(@RequestBody @Valid PersonRequest personRequest) {
        log.info("Inside savePerson method of class PersonController");
        return PersonResponse.builder()
                .username(personRequest.getUsername())
                .message(String.format("Person with username %s is saved successfully", personRequest.getUsername()))
                .build();
    }

    @GetMapping(value = "/{username}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonResponse getPersonByPersonId(@PathVariable(name = "username") String username) {
        log.info("Inside getPersonByPersonId method of class PersonController");
        return PersonResponse.builder().username(username).message(String
                        .format("Person with username %s is found and returned successfully", username))
                .build();
    }
}
