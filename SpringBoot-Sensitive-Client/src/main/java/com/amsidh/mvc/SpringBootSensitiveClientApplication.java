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
        PersonRequest request = new PersonRequest("am", "amsidhlokhande@gmail.com", "pass123");
        PersonResponse response = personClient.savePerson(request);
        System.out.println(response);

        PersonResponse fetchedResponse = personClient.getPersonByUsername("am");
        log.info("Response received {}", new ObjectMapper().writeValueAsString(fetchedResponse));
    }
}
