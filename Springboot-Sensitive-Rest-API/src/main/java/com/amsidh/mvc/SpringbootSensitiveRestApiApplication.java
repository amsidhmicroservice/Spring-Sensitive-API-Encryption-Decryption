package com.amsidh.mvc;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;


@SpringBootApplication
public class SpringbootSensitiveRestApiApplication {
    static {
        // Register BouncyCastle provider
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {

        SpringApplication.run(SpringbootSensitiveRestApiApplication.class, args);
    }

}
