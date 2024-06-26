package com.amsidh.mvc.client;

import com.amsidh.mvc.model.PersonRequest;
import com.amsidh.mvc.model.PersonResponse;
import com.amsidh.mvc.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class PersonClient {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8181/persons";

    public PersonResponse savePerson(PersonRequest personRequest) {
        personRequest.encryptSensitiveData();
        PersonResponse response = restTemplate.postForObject(BASE_URL, personRequest, PersonResponse.class);

        if (response != null) {
            response.decryptSensitiveData();
        }
        return response;
    }

    public PersonResponse getPersonByUsername(String username) {
        String encryptedUsername = CryptoUtil.encrypt(username);

        PersonResponse response = restTemplate.getForObject(BASE_URL + "/" + encryptedUsername, PersonResponse.class);

        if (response != null) {
            response.decryptSensitiveData();
        }
        return response;
    }
}
