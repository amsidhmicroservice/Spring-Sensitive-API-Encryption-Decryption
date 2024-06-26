package com.amsidh.mvc;

import com.amsidh.mvc.client.PersonClient;
import com.amsidh.mvc.model.PersonRequest;
import com.amsidh.mvc.model.PersonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SpringBootSensitiveClientApplication implements CommandLineRunner {

    @Autowired
    private PersonClient personClient;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSensitiveClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        PersonRequest request = new PersonRequest("amsidh", "amsidhlokhande@gmail.com", "password@1234");
        PersonResponse fetchedResponse = personClient.savePerson(request);
        log.info("Response received {}", new ObjectMapper().writeValueAsString(fetchedResponse));

    }
}
